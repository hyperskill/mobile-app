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
        data class AuthWithSocialToken(val authCode: String, val provider: SocialAuthProvider) : Message
        data class AuthWithCode(val authCode: String) : Message
        data class AuthSuccess(val accessToken: String) : Message
        data class AuthError(val errorMsg: String) : Message
    }

    sealed interface Action {
        data class AuthWithSocialToken(val authCode: String, val providerName: String) : Action
        data class AuthWithCode(val authCode: String) : Action
        sealed interface ViewAction : Action {
            object NavigateToHomeScreen : ViewAction
            data class ShowAuthError(val errorMsg: String) : ViewAction
        }
    }
}