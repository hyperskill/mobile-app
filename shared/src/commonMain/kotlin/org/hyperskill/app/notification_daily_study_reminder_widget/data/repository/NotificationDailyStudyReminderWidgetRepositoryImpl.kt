package org.hyperskill.app.notification_daily_study_reminder_widget.data.repository

import org.hyperskill.app.notification_daily_study_reminder_widget.data.source.NotificationDailyStudyReminderWidgetCacheDataSource
import org.hyperskill.app.notification_daily_study_reminder_widget.domain.repository.NotificationDailyStudyReminderWidgetRepository

internal class NotificationDailyStudyReminderWidgetRepositoryImpl(
    private val notificationDailyStudyReminderWidgetCacheDataSource: NotificationDailyStudyReminderWidgetCacheDataSource
) : NotificationDailyStudyReminderWidgetRepository {
    override fun getIsNotificationDailyStudyReminderWidgetHidden(): Boolean =
        notificationDailyStudyReminderWidgetCacheDataSource.getIsNotificationDailyStudyReminderWidgetHidden()

    override fun setIsNotificationDailyStudyReminderWidgetHidden(isHidden: Boolean) {
        notificationDailyStudyReminderWidgetCacheDataSource.setIsNotificationDailyStudyReminderWidgetHidden(isHidden)
    }
}