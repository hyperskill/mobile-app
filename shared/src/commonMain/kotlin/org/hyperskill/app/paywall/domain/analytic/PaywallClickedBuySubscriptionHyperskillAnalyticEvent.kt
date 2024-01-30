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
    private val paywallTransitionSource: PaywallTransitionSource
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Paywall,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.BUY_SUBSCRIPTION
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PaywallAnalyticKeys.PAYWALL_TRANSITION_SOURCE to paywallTransitionSource.analyticName
        )
}