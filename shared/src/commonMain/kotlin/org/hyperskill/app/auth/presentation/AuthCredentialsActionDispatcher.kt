package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.model.AuthException
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Action
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AuthCredentialsActionDispatcher(
    config: ActionDispatcherOptions,
    private val authInteractor: AuthInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.AuthWithEmail -> {
                val result = authInteractor.authWithEmail(action.email, action.password)

                val message =
                    result
                        .map { Message.AuthSuccess }
                        .getOrElse {
                            val errorMessage =
                                if (it is AuthException) {
                                    it.errorMessage
                                } else {
                                    ""
                                }
                            Message.AuthFailure(errorMessage)
                        }

                onNewMessage(message)
            }
        }
    }
}