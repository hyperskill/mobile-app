package org.hyperskill.app.notifications_onboarding.domain.analytics

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents allowing or denying of the system notification permission.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/notifications",
 *     "action": "click",
 *     "part": "notifications_system_notice",
 *     "target": "allow"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class NotificationsOnboardingPermissionResultHyperskillAnalyticsEvent(
    isPermissionGranted: Boolean
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.Notifications,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.NOTIFICATIONS_SYSTEM_NOTICE,
    target = if (isPermissionGranted) HyperskillAnalyticTarget.ALLOW else HyperskillAnalyticTarget.DENY
)