package org.hyperskill.app.auth.presentation

interface AuthFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Authenticated(val authCode: String) : State
    }

    sealed interface Message {
        data class AuthWithGoogle(val accessToken: String) : Message
        data class AuthSuccess(val accessToken: String) : Message
        data class AuthError(val errorMsg: String) : Message
    }

    sealed interface Action {
        data class AuthWithGoogle(val accessToken: String) : Action
        sealed interface ViewAction : Action {
            object NavigateToHomeScreen : ViewAction
            data class ShowAuthError(val errorMsg: String) : ViewAction
        }
    }
}