package org.hyperskill.app.notifications_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on the "Confirm" button of the daily study reminders hour interval picker modal analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/notifications",
 *     "action": "click",
 *     "part": "daily_study_reminders_hour_interval_picker_modal",
 *     "target": "confirm",
 *     "context":
 *     {
 *         "start_hour": 12
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class NotificationsOnboardingDailyStudyRemindersIntervalPickerModalClickedConfirmHyperskillAnalyticEvent(
    private val selectedDailyStudyRemindersStartHour: Int
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.Notifications,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.DAILY_STUDY_REMINDERS_HOUR_INTERVAL_PICKER_MODAL,
    target = HyperskillAnalyticTarget.CONFIRM
) {
    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOf(
                    NotificationsOnboardingAnalyticParams.PARAM_START_HOUR to selectedDailyStudyRemindersStartHour
                )
            )
}