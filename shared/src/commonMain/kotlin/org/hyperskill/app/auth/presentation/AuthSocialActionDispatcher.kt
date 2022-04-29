package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.exception.AuthSocialException
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AuthSocialActionDispatcher(
    config: ActionDispatcherOptions,
    private val authInteractor: AuthInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.AuthWithSocial -> {
                val result = authInteractor.authWithSocial(action.authCode, action.socialAuthProvider)

                val message =
                    result
                        .map { Message.AuthSuccess }
                        .getOrElse {
                            val error =
                                if (it is AuthSocialException) {
                                    it.authSocialError
                                } else {
                                    AuthSocialError.CONNECTION_PROBLEM
                                }
                            Message.AuthFailure(error)
                        }
                onNewMessage(message)
            }
        }
    }
}