package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.presentation.AuthFeature.Action
import org.hyperskill.app.auth.presentation.AuthFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AuthActionDispatcher(
    config: ActionDispatcherOptions
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.AuthWithGoogle -> {
                val result = Result.success("")

                val message =
                    result
                        .map { Message.AuthSuccess(it) }
                        .getOrElse {
                            Message.AuthError(errorMsg = it.message ?: "")
                        }

                onNewMessage(message)
            }
        }
    }
}