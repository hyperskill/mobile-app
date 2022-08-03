package org.hyperskill.app.notification.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource

class NotificationCacheDataSourceImpl(
    private val settings: Settings
) : NotificationCacheDataSource {

    override suspend fun isNotificationsEnabled(): Boolean =
        settings.getBoolean(NotificationCacheKeyValues.NOTIFICATIONS_ENABLED, false)

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        settings.putBoolean(NotificationCacheKeyValues.NOTIFICATIONS_ENABLED, enabled)
    }

    override suspend fun getNotificationTimestamp(key: String): Long =
        settings.getLong(key, 0)

    override suspend fun setNotificationTimestamp(key: String, timestamp: Long) {
        settings.putLong(key, timestamp)
    }
}