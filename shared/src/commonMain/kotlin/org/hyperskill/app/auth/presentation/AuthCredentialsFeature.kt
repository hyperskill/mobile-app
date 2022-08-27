package org.hyperskill.app.auth.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.auth.domain.model.AuthCredentialsError

interface AuthCredentialsFeature {
    data class State(
        val email: String,
        val password: String,
        val formState: FormState
    )

    sealed interface FormState {
        object Editing : FormState
        object Loading : FormState
        data class Error(val credentialsError: AuthCredentialsError) : FormState
        object Authenticated : FormState
    }

    sealed interface Message {
        data class AuthEditing(val email: String, val password: String) : Message
        object SubmitFormClicked : Message
        data class AuthSuccess(val isNewUser: Boolean) : Message
        data class AuthFailure(val credentialsError: AuthCredentialsError) : Message

        /**
         * Analytic
         */
        object AuthViewedEventMessage : Message
        object AuthClickedSignInEventMessage : Message
        object AuthClickedResetPasswordEventMessage : Message
        object AuthClickedContinueWithSocialEventMessage : Message
    }

    sealed interface Action {
        data class AuthWithEmail(val email: String, val password: String) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            data class CompleteAuthFlow(val isNewUser: Boolean) : ViewAction
        }
    }
}