package org.hyperskill.app.paywall.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource

/**
 * Represents a click analytic event of the store product.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/paywall",
 *     "action": "click",
 *     "part": "main",
 *     "target": "store_product",
 *     "context":
 *     {
 *       "source": "login",
 *       "store_product": "premium_mobile:premium-mobile-monthly"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class PaywallClickedProductHyperskillAnalyticEvent(
    paywallTransitionSource: PaywallTransitionSource,
    productId: String
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Paywall,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.STORE_PRODUCT,
    context = mapOf(
        PaywallAnalyticParams.PARAM_TRANSITION_SOURCE to paywallTransitionSource.analyticName,
        PaywallAnalyticParams.STORE_PRODUCT to productId
    )
)