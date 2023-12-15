package org.hyperskill.app.notifications_onboarding.domain.analytic

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
 *     "target": "allow_notifications",
 *     "context":
 *     {
 *         "start_hour": 12
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticEvent(
    private val selectedDailyStudyRemindersStartHour: Int
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.Notifications,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.ALLOW_NOTIFICATIONS
) {
    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOf(
                    NotificationsOnboardingAnalyticParams.PARAM_START_HOUR to selectedDailyStudyRemindersStartHour
                )
            )
}