package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider

interface AuthSocialWebViewFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        object CodeSuccess : State
    }

    sealed interface Message {
        data class AuthCodeSuccess(val authCode: String, val socialAuthProvider: SocialAuthProvider) : Message
        data class AuthFailure(val socialError: AuthSocialError) : Message
    }

    sealed interface Action {
        data class CallbackAuthCode(val authCode: String, val socialAuthProvider: SocialAuthProvider) : Action
        data class CallbackAuthError(val socialError: AuthSocialError) : Action
    }
}
