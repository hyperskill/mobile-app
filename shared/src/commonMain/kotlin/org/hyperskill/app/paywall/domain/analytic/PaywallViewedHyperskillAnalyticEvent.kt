package org.hyperskill.app.paywall.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/paywall",
 *     "action": "view",
 *     "context":
 *     {
 *       "source": "login"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class PaywallViewedHyperskillAnalyticEvent(
    paywallTransitionSource: PaywallTransitionSource
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Paywall,
    action = HyperskillAnalyticAction.VIEW,
    context = mapOf(
        PaywallAnalyticParams.PARAM_TRANSITION_SOURCE to paywallTransitionSource.analyticName
    )
)