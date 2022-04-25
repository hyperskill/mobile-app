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
        object SubmitFormClicked : Message
        object AuthSuccess : Message
        data class AuthFailure(val errorMessage: String) : Message
    }

    sealed interface Action {
        data class AuthWithEmail(val email: String, val password: String) : Action
        sealed interface ViewAction : Action {
            object CompleteAuthFlow : ViewAction
        }
    }
}