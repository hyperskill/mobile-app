package org.hyperskill.app.notification_daily_study_reminder_widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents an analytic event for clicking on a close button in the notification daily study reminder widget.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan/notification-daily-study-reminder-widget",
 *     "action": "click",
 *     "part": "notification_daily_study_reminder_widget",
 *     "target": "close"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object NotificationDailyStudyReminderWidgetClickedCloseHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.StudyPlan.NotificationDailyStudyReminderWidget(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.NOTIFICATION_DAILY_STUDY_REMINDER_WIDGET,
    target = HyperskillAnalyticTarget.CLOSE
)