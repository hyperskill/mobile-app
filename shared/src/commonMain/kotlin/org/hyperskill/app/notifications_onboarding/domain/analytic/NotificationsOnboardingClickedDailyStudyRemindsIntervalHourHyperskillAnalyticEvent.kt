package org.hyperskill.app.notifications_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the daily study reminds formatted time button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/notifications",
 *     "action": "click",
 *     "part": "main",
 *     "target": "daily_study_reminds_time",
 *     "context":
 *     {
 *         "start_hour": 12
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class NotificationsOnboardingClickedDailyStudyRemindsIntervalHourHyperskillAnalyticEvent(
    currentDailyStudyRemindersStartHour: Int
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.Notifications,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.DAILY_STUDY_REMINDS_TIME,
    context = mapOf(
        NotificationsOnboardingAnalyticParams.PARAM_START_HOUR to currentDailyStudyRemindersStartHour
    )
)