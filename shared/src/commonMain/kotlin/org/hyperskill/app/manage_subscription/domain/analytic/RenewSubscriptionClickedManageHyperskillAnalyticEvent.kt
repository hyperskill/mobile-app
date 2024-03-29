package org.hyperskill.app.manage_subscription.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event of the manage subscription button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile/settings/manage-subscription",
 *     "action": "click",
 *     "part": "main",
 *     "target": "renew_subscription"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object RenewSubscriptionClickedManageHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile.Settings.ManageSubscription,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.RENEW_SUBSCRIPTION
)