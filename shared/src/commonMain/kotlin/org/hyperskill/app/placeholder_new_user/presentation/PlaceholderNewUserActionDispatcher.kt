package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Action
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class PlaceholderNewUserActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val trackInteractor: TrackInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val profileInteractor: ProfileInteractor,
    private val freemiumInteractor: FreemiumInteractor
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
                    sentryInteractor.captureErrorMessage("PlaceholderNewUser: tracks.size != tracksProgresses.size")
                }

                val tracksWithProgresses = tracks.map { it.copy(progress = trackProgressById[it.progressId]) }
                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(Message.TracksLoaded.Success(tracksWithProgresses))
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

                if (isFreemiumEnabled) {
                    profileInteractor.selectTrack(currentProfile.id, action.track.id)
                        .getOrElse {
                            return onNewMessage(Message.TrackSelected.Error)
                        }
                } else {
                    val projectByLevel = action.track.projectsByLevel

                    val projectId = projectByLevel.easy?.firstOrNull()
                        ?: projectByLevel.medium?.firstOrNull()
                        ?: projectByLevel.hard?.firstOrNull()
                        ?: projectByLevel.nightmare?.firstOrNull()
                        ?: return onNewMessage(Message.TrackSelected.Error)

                    profileInteractor
                        .selectTrackWithProject(
                            profileId = currentProfile.id,
                            trackId = action.track.id,
                            projectId = projectId
                        )
                        .getOrElse {
                            return onNewMessage(Message.TrackSelected.Error)
                        }
                }

                onNewMessage(Message.TrackSelected.Success)
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}
