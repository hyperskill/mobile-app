package org.hyperskill.app.profile_settings.domain.analytic.subscription

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the subscription suggestion details.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "main",
 *     "target": "subscription_suggestion_details"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object SubscriptionSuggestionDetailsClickedHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile.Settings(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.SUBSCRIPTION_SUGGESTION_DETAILS
)