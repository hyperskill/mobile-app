package org.hyperskill.app.track_list.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_list.presentation.TrackListFeature.Action
import org.hyperskill.app.track_list.presentation.TrackListFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TrackListActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val trackInteractor: TrackInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val profileInteractor: ProfileInteractor,
    private val freemiumInteractor: FreemiumInteractor,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.Initialize -> {
                val sentryTransaction =
                    HyperskillSentryTransactionBuilder.buildPlaceholderNewUserScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val tracks = trackInteractor
                    .getAllTracks()
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, it)
                        return onNewMessage(Message.TracksLoaded.Error)
                    }
                    .filter { it.isBeta.not() }

                val trackProgressById = progressesInteractor
                    .getTracksProgresses(tracks.map { it.id }, forceLoadFromRemote = true)
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, it)
                        return onNewMessage(Message.TracksLoaded.Error)
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
                        return onNewMessage(Message.TracksLoaded.Error)
                    }
                    .trackId

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(Message.TracksLoaded.Success(tracksWithProgresses, currentTrackId))
            }
            is Action.SelectTrack -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        return onNewMessage(Message.TrackSelected.Error)
                    }

                val isFreemiumEnabled = freemiumInteractor
                    .isFreemiumEnabled()
                    .getOrElse {
                        return onNewMessage(Message.TrackSelected.Error)
                    }

                profileInteractor.selectTrack(currentProfile.id, action.track.id)
                    .getOrElse {
                        return onNewMessage(Message.TrackSelected.Error)
                    }

                onNewMessage(Message.TrackSelected.Success)

                if (!isFreemiumEnabled && action.track.projects.isNotEmpty()) {
                    onNewMessage(Message.ProjectSelectionRequired(action.track))
                }
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}
