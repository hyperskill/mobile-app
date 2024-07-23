package org.hyperskill.app.paywall.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource

/**
 * Represents a click analytic event of the close button on the Paywall screen.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/paywall",
 *     "action": "click",
 *     "part": "main",
 *     "target": "close",
 *     "context":
 *     {
 *       "source": "login"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class PaywallClickedCloseButtonHyperskillAnalyticEvent(
    paywallTransitionSource: PaywallTransitionSource
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Paywall,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.CLOSE,
    context = mapOf(
        PaywallAnalyticParams.PARAM_TRANSITION_SOURCE to paywallTransitionSource.analyticName
    )
)