package org.hyperskill.app.auth.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.auth.domain.model.AuthCredentialsError
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb

interface AuthCredentialsFeature {
    data class State(
        val email: String,
        val password: String,
        val formState: FormState,
        val isLoadingMagicLink: Boolean = false
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
        data class AuthSuccess(val profile: Profile) : Message
        data class AuthFailure(val credentialsError: AuthCredentialsError, val originalError: Throwable) : Message

        object ClickedResetPassword : Message

        data class GetMagicLinkReceiveSuccess(val url: String) : Message
        object GetMagicLinkReceiveFailure : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedSignInEventMessage : Message
        object ClickedContinueWithSocialEventMessage : Message
    }

    sealed interface Action {
        data class AuthWithEmail(val email: String, val password: String) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class GetMagicLink(val path: HyperskillUrlPath) : Action

        /**
         * Sentry
         */
        data class AddSentryBreadcrumb(val breadcrumb: HyperskillSentryBreadcrumb) : Action
        data class CaptureSentryException(val throwable: Throwable) : Action

        sealed interface ViewAction : Action {
            data class CompleteAuthFlow(val profile: Profile) : ViewAction
            data class OpenUrl(val url: String) : ViewAction
            object ShowGetMagicLinkError : ViewAction
        }
    }
}