package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on "Continue with social networks" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/login/password",
 *     "action": "click",
 *     "part": "main",
 *     "target": "continue_with_social_networks"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class AuthCredentialsClickedContinueWithSocialHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Login.Password(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.CONTINUE_WITH_SOCIAL_NETWORKS
)