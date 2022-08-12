package org.hyperskill.app.notification.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.SharedResources.strings
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.notification.data.model.Notification
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource

class NotificationCacheDataSourceImpl(
    private val settings: Settings,
    private val resourceProvider: ResourceProvider
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

    override fun getRandomNotification(): Notification {
        val notifications = listOf(
            Notification(resourceProvider.getString(strings.notification1_title), resourceProvider.getString(strings.notification1_desc)),
            Notification(resourceProvider.getString(strings.notification2_title), resourceProvider.getString(strings.notification2_desc)),
            Notification(resourceProvider.getString(strings.notification3_title), resourceProvider.getString(strings.notification3_desc)),
            Notification(resourceProvider.getString(strings.notification4_title), resourceProvider.getString(strings.notification4_desc)),
            Notification(resourceProvider.getString(strings.notification5_title), resourceProvider.getString(strings.notification5_desc)),
            Notification(resourceProvider.getString(strings.notification6_title), resourceProvider.getString(strings.notification6_desc)),
            Notification(resourceProvider.getString(strings.notification7_title), resourceProvider.getString(strings.notification7_desc)),
            Notification(resourceProvider.getString(strings.notification8_title), resourceProvider.getString(strings.notification8_desc)),
            Notification(resourceProvider.getString(strings.notification9_title), resourceProvider.getString(strings.notification9_desc)),
            Notification(resourceProvider.getString(strings.notification10_title), resourceProvider.getString(strings.notification10_desc)),
            Notification(resourceProvider.getString(strings.notification11_title), resourceProvider.getString(strings.notification11_desc)),
            Notification(resourceProvider.getString(strings.notification12_title), resourceProvider.getString(strings.notification12_desc)),
            Notification(resourceProvider.getString(strings.notification13_title), resourceProvider.getString(strings.notification13_desc)),
            Notification(resourceProvider.getString(strings.notification14_title), resourceProvider.getString(strings.notification14_desc)),
        )

        return notifications.shuffled().first()
    }
}