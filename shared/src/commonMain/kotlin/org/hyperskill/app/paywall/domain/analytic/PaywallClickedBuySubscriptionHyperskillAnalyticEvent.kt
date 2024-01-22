package org.hyperskill.app.paywall.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event of the buy subscription button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/paywall",
 *     "action": "click",
 *     "part": "main",
 *     "target": "buy_subscription"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object PaywallClickedBuySubscriptionHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Paywall,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.BUY_SUBSCRIPTION
)