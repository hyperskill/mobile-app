package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.auth.domain.model.AuthCredentialsError

/**
 * Represents a failed authentication with credential event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/login",
 *     "action": "error",
 *     "part": "main",
 *     "context": {
 *         "error_message": "wrong_credentials / empty_email / empty_password / unknown"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class AuthCredentialsFailedHyperskillAnalyticEvent(
    error: AuthCredentialsError
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Login(),
    action = HyperskillAnalyticAction.ERROR,
    part = HyperskillAnalyticPart.MAIN,
    context = mapOf(
        AuthAnalyticKeys.ERROR_MESSAGE to when (error) {
            AuthCredentialsError.ERROR_CREDENTIALS_AUTH -> "wrong_credentials"
            AuthCredentialsError.ERROR_EMAIL_EMPTY -> "empty_email"
            AuthCredentialsError.ERROR_PASSWORD_EMPTY -> "empty_password"
            AuthCredentialsError.CONNECTION_PROBLEM -> "unknown"
        }
    )
)