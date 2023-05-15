package org.hyperskill.app.track_selection.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackSelectionListActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val trackInteractor: TrackInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val profileInteractor: ProfileInteractor,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.Initialize -> {
                val sentryTransaction =
                    HyperskillSentryTransactionBuilder.buildTrackSelectionListScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val tracks = trackInteractor
                    .getAllTracks()
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, it)
                        return onNewMessage(TrackSelectionListFeature.TracksFetchResult.Error)
                    }

                val trackProgressById = progressesInteractor
                    .getTracksProgresses(tracks.map { it.id }, forceLoadFromRemote = true)
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, it)
                        return onNewMessage(TrackSelectionListFeature.TracksFetchResult.Error)
                    }
                    .associateBy { it.id }

                if (tracks.size != trackProgressById.size) {
                    sentryInteractor.captureErrorMessage("TrackList: tracks.size != tracksProgresses.size")
                }

                val tracksWithProgresses = tracks.mapNotNull { track ->
                    trackProgressById[track.progressId]?.let { trackProgress ->
                        TrackWithProgress(
                            track = track,
                            trackProgress = trackProgress
                        )
                    }
                }

                val currentTrackId = currentStudyPlanStateRepository.getState()
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, it)
                        return onNewMessage(TrackSelectionListFeature.TracksFetchResult.Error)
                    }
                    .trackId

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(TrackSelectionListFeature.TracksFetchResult.Success(tracksWithProgresses, currentTrackId))
            }
            is InternalAction.SelectTrack -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        return onNewMessage(TrackSelectionListFeature.TrackSelectionResult.Error)
                    }

                profileInteractor.selectTrack(currentProfile.id, action.track.id)
                    .getOrElse {
                        return onNewMessage(TrackSelectionListFeature.TrackSelectionResult.Error)
                    }

                onNewMessage(TrackSelectionListFeature.TrackSelectionResult.Success)
            }
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}