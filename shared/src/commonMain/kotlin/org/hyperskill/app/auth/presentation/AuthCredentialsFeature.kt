package org.hyperskill.app.auth.presentation

interface AuthCredentialsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        object Authenticated : State
    }

    sealed interface Message {
        data class AuthWithEmail(val email: String, val password: String) : Message
        object AuthSuccess : Message
        object AuthFailure : Message
    }

    sealed interface Action {
        data class AuthWithEmail(val email: String, val password: String) : Action
        sealed interface ViewAction : Action {
            object NavigateToHomeScreen : ViewAction
            object ShowAuthError : ViewAction
        }
    }
}