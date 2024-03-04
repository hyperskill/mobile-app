package org.hyperskill.app.paywall.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource

/**
 * Represents a click analytic event of the continue-with-limits button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/paywall",
 *     "action": "click",
 *     "part": "main",
 *     "target": "continue_with_limits",
 *     "context":
 *     {
 *       "source": "login"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class PaywallClickedContinueWithLimitsHyperskillAnalyticEvent(
    paywallTransitionSource: PaywallTransitionSource
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Paywall,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.CONTINUE_WITH_LIMITS,
    context = mapOf(
        PaywallAnalyticParams.PARAM_TRANSITION_SOURCE to paywallTransitionSource.analyticName
    )
)