package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.presentation.AuthFeature.Action
import org.hyperskill.app.auth.presentation.AuthFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AuthActionDispatcher(
    config: ActionDispatcherOptions,
    private val authInteractor: AuthInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.AuthWithSocialToken -> {
                val result = authInteractor.authWithSocialToken(action.authCode, action.providerName)

                val message =
                    result
                        .map { Message.AuthSuccess(action.authCode) }
                        .getOrElse {
                            Message.AuthError(errorMsg = it.message ?: "")
                        }

                onNewMessage(message)
            }
            is Action.AuthWithCode -> {
                val result = authInteractor.authWithCode(action.authCode)

                val message =
                    result
                        .map { Message.AuthSuccess(action.authCode) }
                        .getOrElse {
                            Message.AuthError(errorMsg = it.message ?: "")
                        }

                onNewMessage(message)
            }
        }
    }
}