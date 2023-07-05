package org.hyperskill.app.track_selection.list.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.progress_screen.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackSelectionListActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val trackInteractor: TrackInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchTracks ->
                coroutineScope {
                    val sentryTransaction =
                        HyperskillSentryTransactionBuilder.buildTrackSelectionListScreenRemoteDataLoading()
                    sentryInteractor.startTransaction(sentryTransaction)

                    val studyPlanDeferred = async { currentStudyPlanStateRepository.getState() }
                    val tracksDeferred = async { trackInteractor.getAllTracks() }

                    val currentTrackId = studyPlanDeferred.await()
                        .getOrElse {
                            sentryInteractor.finishTransaction(sentryTransaction, it)
                            return@coroutineScope onNewMessage(TrackSelectionListFeature.TracksFetchResult.Error)
                        }
                        .trackId

                    val tracks = tracksDeferred.await()
                        .getOrElse {
                            sentryInteractor.finishTransaction(sentryTransaction, it)
                            return@coroutineScope onNewMessage(TrackSelectionListFeature.TracksFetchResult.Error)
                        }
                    val trackProgressById = progressesInteractor
                        .getTracksProgresses(tracks.map { it.id }, forceLoadFromRemote = true)
                        .getOrElse {
                            sentryInteractor.finishTransaction(sentryTransaction, it)
                            return@coroutineScope onNewMessage(TrackSelectionListFeature.TracksFetchResult.Error)
                        }
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

                    sentryInteractor.finishTransaction(sentryTransaction)

                    onNewMessage(
                        TrackSelectionListFeature.TracksFetchResult.Success(
                            tracks = tracksWithProgresses,
                            selectedTrackId = currentTrackId
                        )
                    )
                }
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}