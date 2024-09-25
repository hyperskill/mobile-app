package org.hyperskill.app.notification_daily_study_reminder_widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event of the notification daily study reminder widget.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan/notification-daily-study-reminder-widget",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object NotificationDailyStudyReminderWidgetViewedHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.StudyPlan.NotificationDailyStudyReminderWidget(),
    action = HyperskillAnalyticAction.VIEW
)