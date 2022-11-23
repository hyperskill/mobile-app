package org.hyperskill.app.sentry.domain.model.breadcrumb

import org.hyperskill.app.auth.domain.model.AuthCredentialsError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel

object HyperskillSentryBreadcrumbBuilder {
    /**
     * AuthCredentials
     */
    fun buildAuthCredentialsSigningIn(): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.AUTH_CREDENTIALS,
            message = "Signing in with log/pas",
            level = HyperskillSentryLevel.INFO
        )

    fun buildAuthCredentialsSignedInSuccessfully(): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.AUTH_CREDENTIALS,
            message = "Signed in with log/pas",
            level = HyperskillSentryLevel.INFO
        )

    fun buildAuthCredentialsSignInFailed(preconditionCheckFormStateError: AuthCredentialsError? = null): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.AUTH_CREDENTIALS,
            message = "Sign in with log/pas failed",
            level = HyperskillSentryLevel.ERROR,
            data = if (preconditionCheckFormStateError != null) mapOf("form_state_error" to preconditionCheckFormStateError.toString()) else null
        )

    /**
     * AuthSocial
     */
    fun buildAuthSocialSigningIn(socialAuthProvider: SocialAuthProvider): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.AUTH_SOCIAL,
            message = "Signing in with ${socialAuthProvider.title}",
            level = HyperskillSentryLevel.INFO
        )

    fun buildAuthSocialSignedInSuccessfully(socialAuthProvider: SocialAuthProvider): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.AUTH_SOCIAL,
            message = "Signed in with ${socialAuthProvider.title}",
            level = HyperskillSentryLevel.INFO
        )

    fun buildAuthSocialSignInFailed(socialAuthProvider: SocialAuthProvider): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.AUTH_SOCIAL,
            message = "Sign in failed with ${socialAuthProvider.title}",
            level = HyperskillSentryLevel.ERROR
        )
}