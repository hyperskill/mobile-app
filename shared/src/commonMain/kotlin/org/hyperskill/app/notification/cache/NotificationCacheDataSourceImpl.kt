package org.hyperskill.app.notification.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource

class NotificationCacheDataSourceImpl(
    private val settings: Settings
) : NotificationCacheDataSource {
    override suspend fun getNotificationsEnabled(): Boolean {
        return settings[NotificationCacheKeyValues.NOTIFICATIONS_ENABLED, false]
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        settings.putBoolean(NotificationCacheKeyValues.NOTIFICATIONS_ENABLED, enabled)
    }

    override suspend fun getNotificationsTimestamp(): Int {
        return settings[NotificationCacheKeyValues.NOTIFICATIONS_TIMESTAMP, 0]
    }

    override suspend fun setNotificationsTimestamp(timestamp: Int) {
        settings.putInt(NotificationCacheKeyValues.NOTIFICATIONS_TIMESTAMP, timestamp)
    }
}