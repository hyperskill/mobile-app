package org.hyperskill.app.auth.presentation

import org.hyperskill.app.auth.domain.model.SocialAuthProvider

interface AuthFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Authenticated(val authCode: String) : State
    }

    sealed interface Message {
        data class AuthWithEmail(val email: String, val password: String) : Message
        data class AuthWithSocial(val authCode: String, val socialProvider: SocialAuthProvider) : Message
        data class AuthSuccess(val accessToken: String) : Message
        data class AuthError(val errorMsg: String) : Message
    }

    sealed interface Action {
        data class AuthWithEmail(val email: String, val password: String) : Action
        data class AuthWithSocial(val authCode: String, val socialProvider: SocialAuthProvider) : Action
        sealed interface ViewAction : Action {
            object NavigateToHomeScreen : ViewAction
            data class ShowAuthError(val errorMsg: String) : ViewAction
        }
    }
}