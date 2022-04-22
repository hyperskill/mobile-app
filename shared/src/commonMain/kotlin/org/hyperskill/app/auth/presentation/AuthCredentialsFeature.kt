package org.hyperskill.app.auth.presentation

interface AuthCredentialsFeature {
    data class State(
        val email: String,
        val password: String,
        val formState: FormState
    )

    sealed interface FormState {
        object Editing : FormState
        object Loading : FormState
        data class Error(val error: String) : FormState
        object Authenticated : FormState
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