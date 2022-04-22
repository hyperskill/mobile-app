package org.hyperskill.app.auth.presentation

interface AuthCredentialsFeature {
    sealed interface State {
        object Idle : State
        data class Editing(override val email: String, override val password: String) : State, HasInputFields
        data class Loading(override val email: String, override val password: String) : State, HasInputFields
        data class Error(override val email: String, override val password: String) : State, HasInputFields
        object Authenticated : State
    }

    sealed interface HasInputFields {
        val email: String
        val password: String
    }

    sealed interface Message {
        data class AuthEditing(val email: String, val password: String) : Message
        object AuthWithEmail : Message
        object AuthSuccess : Message
        object AuthFailure : Message
    }

    sealed interface Action {
        data class AuthWithEmail(val email: String, val password: String) : Action
        sealed interface ViewAction : Action {
            object NavigateToHomeScreen : ViewAction
        }
    }
}