package org.hyperskill.app.notification_daily_study_reminder_widget.data.source

interface NotificationDailyStudyReminderWidgetCacheDataSource {
    fun getIsNotificationDailyStudyReminderWidgetHidden(): Boolean
    fun setIsNotificationDailyStudyReminderWidgetHidden(isHidden: Boolean)
}