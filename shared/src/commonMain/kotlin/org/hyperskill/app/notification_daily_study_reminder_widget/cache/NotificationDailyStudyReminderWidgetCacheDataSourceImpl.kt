package org.hyperskill.app.notification_daily_study_reminder_widget.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.notification_daily_study_reminder_widget.data.source.NotificationDailyStudyReminderWidgetCacheDataSource

internal class NotificationDailyStudyReminderWidgetCacheDataSourceImpl(
    private val settings: Settings
) : NotificationDailyStudyReminderWidgetCacheDataSource {
    override fun getIsNotificationDailyStudyReminderWidgetHidden(): Boolean =
        settings.getBoolean(
            NotificationDailyStudyReminderWidgetCacheKeyValues.NOTIFICATION_DAILY_STUDY_REMINDER_WIDGET_HIDDEN,
            false
        )

    override fun setIsNotificationDailyStudyReminderWidgetHidden(isHidden: Boolean) {
        settings.putBoolean(
            NotificationDailyStudyReminderWidgetCacheKeyValues.NOTIFICATION_DAILY_STUDY_REMINDER_WIDGET_HIDDEN,
            isHidden
        )
    }
}