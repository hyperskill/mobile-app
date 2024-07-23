package org.hyperskill.app.track_selection.list.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.details.domain.repository.TrackSelectionDetailsRepository
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class TrackSelectionListActionDispatcher(
    config: ActionDispatcherOptions,
    private val sentryInteractor: SentryInteractor,
    private val trackInteractor: TrackInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val trackSelectionDetailsRepository: TrackSelectionDetailsRepository,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchTracks ->
                handleFetchTracksAction(::onNewMessage)
            else -> {}
        }
    }

    private suspend fun handleFetchTracksAction(
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildTrackSelectionListScreenRemoteDataLoading(),
            onError = { TrackSelectionListFeature.TracksFetchResult.Error }
        ) {
            coroutineScope {
                val studyPlanDeferred = async { currentStudyPlanStateRepository.getState() }
                val tracksDeferred = async { trackInteractor.getAllTracks() }

                val currentTrackId = studyPlanDeferred.await()
                    .getOrThrow()
                    .trackId

                val tracks = tracksDeferred.await().getOrThrow()
                val trackProgressById = progressesInteractor
                    .getTracksProgresses(tracks.map { it.id }, forceLoadFromRemote = true)
                    .getOrThrow()
                    .associateBy { it.id }

                if (tracks.size != trackProgressById.size) {
                    sentryInteractor.captureErrorMessage("TrackSelectionList: tracks.size != tracksProgresses.size")
                }

                val tracksWithProgresses = tracks.mapNotNull { track ->
                    trackProgressById[track.progressId]?.let { trackProgress ->
                        TrackWithProgress(
                            track = track,
                            trackProgress = trackProgress
                        )
                    }
                }

                TrackSelectionListFeature.TracksFetchResult.Success(
                    tracks = tracksWithProgresses,
                    selectedTrackId = currentTrackId,
                    tracksSelectionCountMap = trackSelectionDetailsRepository.getTracksSelectionCountMap()
                )
            }
        }.let(onNewMessage)
    }
}