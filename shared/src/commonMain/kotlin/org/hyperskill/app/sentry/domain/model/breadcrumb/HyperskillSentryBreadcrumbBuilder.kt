package org.hyperskill.app.sentry.domain.model.breadcrumb

import org.hyperskill.app.auth.domain.model.AuthCredentialsError
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel

object HyperskillSentryBreadcrumbBuilder {
    /**
     * AuthCredentials
     */
    fun buildAuthCredentialsSigningIn(): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.CATEGORY_AUTH_CREDENTIALS,
            message = "Signing in with log/pas",
            level = HyperskillSentryLevel.INFO
        )

    fun buildAuthCredentialsSignedInSuccessfully(): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.CATEGORY_AUTH_CREDENTIALS,
            message = "Signed in with log/pas",
            level = HyperskillSentryLevel.INFO
        )

    fun buildAuthCredentialsSignInFailed(preconditionCheckFormStateError: AuthCredentialsError? = null): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.CATEGORY_AUTH_CREDENTIALS,
            message = "Sign in with log/pas failed",
            level = HyperskillSentryLevel.INFO,
            data = if (preconditionCheckFormStateError != null) mapOf("form_state_error" to preconditionCheckFormStateError.toString()) else null
        )
}