package org.hyperskill.app.notification.data.repository

import org.hyperskill.app.notification.data.model.Notification
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource
import org.hyperskill.app.notification.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val notificationCacheDataSource: NotificationCacheDataSource
) : NotificationRepository {
    override fun isNotificationsEnabled(): Boolean =
        notificationCacheDataSource.isNotificationsEnabled()

    override fun setNotificationsEnabled(enabled: Boolean) {
        notificationCacheDataSource.setNotificationsEnabled(enabled)
    }

    override fun getNotificationTimestamp(key: String): Long =
        notificationCacheDataSource.getNotificationTimestamp(key)

    override fun setNotificationTimestamp(key: String, timestamp: Long) {
        notificationCacheDataSource.setNotificationTimestamp(key, timestamp)
    }

    override fun getRandomNotification(): Notification =
        notificationCacheDataSource.getRandomNotification()
}