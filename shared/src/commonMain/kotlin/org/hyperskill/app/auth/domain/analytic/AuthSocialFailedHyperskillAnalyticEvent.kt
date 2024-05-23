package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider

/**
 * Represents a failed authentication via social auth provider event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/login",
 *     "action": "error",
 *     "part": "main",
 *     "target": "jetbrains_account / google / github / apple",
 *     "context": {
 *         "error_message": "unknown / This email already in use / etc"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class AuthSocialFailedHyperskillAnalyticEvent(
    error: AuthSocialError?,
    provider: SocialAuthProvider
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Login(),
    action = HyperskillAnalyticAction.ERROR,
    part = HyperskillAnalyticPart.MAIN,
    target = provider.getAnalyticTarget(),
    context = mapOf(
        AuthAnalyticKeys.ERROR_MESSAGE to when (error) {
            AuthSocialError.ConnectionProblem, null -> "unknown"
            is AuthSocialError.ServerError -> error.errorText
        }
    )
)