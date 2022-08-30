package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider

interface AuthSocialFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        object Authenticated : State
    }

    sealed interface Message {
        data class AuthWithSocial(
            val authCode: String,
            val idToken: String? = null,
            val socialAuthProvider: SocialAuthProvider
        ) : Message

        data class AuthSuccess(val isNewUser: Boolean) : Message
        data class AuthFailure(val socialError: AuthSocialError) : Message
    }

    sealed interface Action {
        data class AuthWithSocial(
            val authCode: String,
            val idToken: String?,
            val socialAuthProvider: SocialAuthProvider
        ) : Action

        sealed interface ViewAction : Action {
            data class CompleteAuthFlow(val isNewUser: Boolean) : ViewAction
            data class ShowAuthError(val socialError: AuthSocialError) : ViewAction
        }
    }
}