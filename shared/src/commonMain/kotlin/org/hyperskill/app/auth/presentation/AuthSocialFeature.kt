package org.hyperskill.app.auth.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb

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

        data class AuthSuccess(val socialAuthProvider: SocialAuthProvider, val isNewUser: Boolean) : Message
        data class AuthFailure(val data: AuthFailureData) : Message

        /**
         * A message that indicates about social auth provider failed sing in attempt via SDK or OAuth.
         *
         * @property data The failure data.
         *
         * @see AuthFailureData
         */
        data class SocialAuthProviderAuthFailureEventMessage(val data: AuthFailureData) : Message

        /**
         * Represents a failure auth data.
         *
         * @property socialAuthProvider The social auth provider.
         * @property socialAuthError The encapsulated social auth module error.
         * @property originalError The original error that failed auth attempt.
         *
         * @see SocialAuthProvider
         * @see AuthSocialError
         */
        data class AuthFailureData(
            val socialAuthProvider: SocialAuthProvider,
            val socialAuthError: AuthSocialError?,
            val originalError: Throwable
        )

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        data class ClickedSignInWithSocialEventMessage(val socialAuthProvider: SocialAuthProvider) : Message
        object ClickedContinueWithEmailEventMessage : Message
    }

    sealed interface Action {
        data class AuthWithSocial(
            val authCode: String,
            val idToken: String?,
            val socialAuthProvider: SocialAuthProvider
        ) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        /**
         * Sentry
         */
        data class AddSentryBreadcrumb(val breadcrumb: HyperskillSentryBreadcrumb) : Action
        data class CaptureSentryAuthError(val socialAuthError: AuthSocialError?, val originalError: Throwable) : Action

        sealed interface ViewAction : Action {
            data class CompleteAuthFlow(val isNewUser: Boolean) : ViewAction
            data class ShowAuthError(val socialAuthError: AuthSocialError, val originalError: Throwable) : ViewAction
        }
    }
}