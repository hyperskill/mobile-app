package org.hyperskill.app.notification.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource

class NotificationCacheDataSourceImpl(
    private val settings: Settings
) : NotificationCacheDataSource {

    override fun isNotificationsEnabled(): Boolean =
        settings.getBoolean(NotificationCacheKeyValues.NOTIFICATIONS_ENABLED, false)

    override fun setNotificationsEnabled(enabled: Boolean) {
        settings.putBoolean(NotificationCacheKeyValues.NOTIFICATIONS_ENABLED, enabled)
    }

    override fun getNotificationTimestamp(key: String): Long =
        settings.getLong(key, 0)

    override fun setNotificationTimestamp(key: String, timestamp: Long) {
        settings.putLong(key, timestamp)
    }

    override fun getDailyStudyRemindersIntervalStartHour(): Int =
        settings.getInt(NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_START_HOUR, NotificationCacheKeyValues.NOTIFICATION_DAILY_REMINDER_DEFAULT_START_HOUR)

    override fun setDailyStudyRemindersIntervalStartHour(hour: Int) {
        settings.putInt(NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_START_HOUR, hour)
    }
}