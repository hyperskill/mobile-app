package org.hyperskill.app.notification_onboarding.domain.analytics

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Allow notifications" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/notifications",
 *     "action": "click",
 *     "part": "main",
 *     "target": "allow_notifications_button"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.Notifications,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.ALLOW_NOTIFICATIONS
)