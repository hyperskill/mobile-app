package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Action
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class PlaceholderNewUserActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val trackInteractor: TrackInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val profileInteractor: ProfileInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.Initialize -> {
                val tracks = trackInteractor
                    .getAllTracks()
                    .getOrElse {
                        onNewMessage(Message.TracksLoaded.Error)
                        return
                    }
                    .filter { it.isBeta.not() }

                val tracksProgresses = progressesInteractor
                    .getTracksProgresses(tracks.map { it.id })
                    .getOrElse {
                        onNewMessage(Message.TracksLoaded.Error)
                        return
                    }
                    .associateBy { it.vid.split('-').last().toLongOrNull() ?: 0 }

                onNewMessage(Message.TracksLoaded.Success(tracks, tracksProgresses))
            }
            is Action.SelectTrack -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        onNewMessage(Message.TrackSelected.Error)
                        return
                    }

                val projectByLevel = action.track.projectsByLevel

                val projectId = projectByLevel.easy?.firstOrNull()
                    ?: projectByLevel.medium?.firstOrNull()
                    ?: projectByLevel.hard?.firstOrNull()
                    ?: projectByLevel.nightmare?.firstOrNull()

                if (projectId == null) {
                    onNewMessage(Message.TrackSelected.Error)
                    return
                }

                profileInteractor
                    .selectTrackWithProject(
                        profileId = currentProfile.id,
                        trackId = action.track.id,
                        projectId = projectId
                    )
                    .getOrElse {
                        onNewMessage(Message.TrackSelected.Error)
                        return
                    }

                onNewMessage(Message.TrackSelected.Success)
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }
}
