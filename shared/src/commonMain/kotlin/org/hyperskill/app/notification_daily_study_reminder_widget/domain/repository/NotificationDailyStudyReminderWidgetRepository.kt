package org.hyperskill.app.notification_daily_study_reminder_widget.domain.repository

interface NotificationDailyStudyReminderWidgetRepository {
    fun getIsNotificationDailyStudyReminderWidgetHidden(): Boolean
    fun setIsNotificationDailyStudyReminderWidgetHidden(isHidden: Boolean)
}