package org.hyperskill.app.paywall.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource

/**
 * Represents a click analytic event of the buy subscription button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/paywall",
 *     "action": "click",
 *     "part": "main",
 *     "target": "buy_subscription",
 *     "context":
 *     {
 *       "source": "login"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class PaywallClickedBuySubscriptionHyperskillAnalyticEvent(
    paywallTransitionSource: PaywallTransitionSource
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Paywall,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.BUY_SUBSCRIPTION,
    context = mapOf(
        PaywallAnalyticParams.PARAM_TRANSITION_SOURCE to paywallTransitionSource.analyticName
    )
)