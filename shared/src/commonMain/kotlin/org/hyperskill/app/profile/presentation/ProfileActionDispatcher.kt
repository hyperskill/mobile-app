package org.hyperskill.app.profile.presentation

import kotlinx.coroutines.launch
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileInteractor: ProfileInteractor,
    private val streakInteractor: StreakInteractor,
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        actionScope.launch {
            profileInteractor.solvedStepsSharedFlow.collect {
                onNewMessage(Message.StepSolved(it))
            }
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchCurrentProfile -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.REMOTE)
                    .getOrElse {
                        onNewMessage(Message.ProfileLoaded.Error(it.message ?: ""))
                        return
                    }

                val message = streakInteractor
                    .getStreaks(currentProfile.id)
                    .map { Message.ProfileLoaded.Success(currentProfile, it.firstOrNull()) }
                    .getOrElse { Message.ProfileLoaded.Error(it.message ?: "") }

                onNewMessage(message)
            }
            is Action.FetchProfile -> {
                // TODO add code when GET on any profile is implemented
            }
            is Action.UpdateStreakInfo -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse { return }

                val message = streakInteractor
                    .getStreaks(currentProfile.id)
                    .map { Message.ProfileLoaded.Success(currentProfile, it.firstOrNull()) }
                    .getOrElse { return }

                onNewMessage(message)
            }
        }
    }
}