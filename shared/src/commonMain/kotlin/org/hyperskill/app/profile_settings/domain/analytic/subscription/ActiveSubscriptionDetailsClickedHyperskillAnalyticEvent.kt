package org.hyperskill.app.profile_settings.domain.analytic.subscription

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the active subscription details.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "main",
 *     "target": "active_subscription_details"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object ActiveSubscriptionDetailsClickedHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile.Settings(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.ACTIVE_SUBSCRIPTION_DETAILS
)