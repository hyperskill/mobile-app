package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Action
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class PlaceholderNewUserActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val trackInteractor: TrackInteractor,
    private val profileInteractor: ProfileInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    companion object {
        private const val DEFAULT_PROJECT_ID: Long = 8
    }

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

                onNewMessage(Message.TracksLoaded.Success(tracks))
            }
            is Action.SelectTrack -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        onNewMessage(Message.TrackSelected.Error)
                        return
                    }

                profileInteractor
                    .selectTrackWithProject(
                        profileId = currentProfile.id,
                        trackId = action.track.id,
                        projectId = action.track.projectsByLevel.easy?.firstOrNull() ?: DEFAULT_PROJECT_ID
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
