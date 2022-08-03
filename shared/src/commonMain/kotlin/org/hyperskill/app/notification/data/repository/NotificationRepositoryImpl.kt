package org.hyperskill.app.notification.data.repository

import org.hyperskill.app.notification.data.source.NotificationCacheDataSource
import org.hyperskill.app.notification.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val notificationCacheDataSource: NotificationCacheDataSource
) : NotificationRepository {
    override suspend fun isNotificationsEnabled(): Boolean =
        notificationCacheDataSource.isNotificationsEnabled()

    override suspend fun setNotificationsEnabled(enabled: Boolean): Unit =
        notificationCacheDataSource.setNotificationsEnabled(enabled)

    override suspend fun getNotificationTimestamp(key: String): Long =
        notificationCacheDataSource.getNotificationTimestamp(key)

    override suspend fun setNotificationTimestamp(key: String, timestamp: Long): Unit =
        notificationCacheDataSource.setNotificationTimestamp(key, timestamp)
}