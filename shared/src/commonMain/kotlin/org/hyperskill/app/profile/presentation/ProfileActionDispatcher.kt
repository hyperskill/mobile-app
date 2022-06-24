package org.hyperskill.app.profile.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message

class ProfileActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileInteractor: ProfileInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchCurrentProfile -> {
                val result = profileInteractor.getCurrentProfile()

                val message =
                    result
                        .map {
                            Message.ProfileLoaded.Success(it)
                        }
                        .getOrElse {
                            Message.ProfileLoaded.Error(errorMsg = it.message ?: "")
                        }

                onNewMessage(message)
            }

            is Action.FetchProfile -> {
                // TODO add code when GET on any profile is implemented
            }
        }
    }
}