package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider

interface AuthSocialWebViewFeature {
    sealed interface State {
        object Idle : State
        data class Loading(val url: String) : State
        object Content : State
    }

    sealed interface Message {
        data class InitMessage(val url: String) : Message
        object PageLoaded : Message
        data class AuthCodeSuccess(val authCode: String, val socialAuthProvider: SocialAuthProvider) : Message
        data class AuthCodeFailure(val socialError: AuthSocialError, val originalError: Throwable?) : Message
    }

    sealed interface Action {
        data class CallbackAuthCode(val authCode: String, val socialAuthProvider: SocialAuthProvider) : Action
        data class CallbackAuthError(val socialError: AuthSocialError, val originalError: Throwable?) : Action
    }
}
