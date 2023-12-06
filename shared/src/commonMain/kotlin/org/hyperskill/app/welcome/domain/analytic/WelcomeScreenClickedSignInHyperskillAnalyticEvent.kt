package org.hyperskill.app.welcome.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Sign in" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding",
 *     "action": "click",
 *     "part": "main",
 *     "target": "sign_in"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class WelcomeScreenClickedSignInHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.SIGN_IN
)