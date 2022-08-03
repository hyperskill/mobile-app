package org.hyperskill.app.notification.domain

import org.hyperskill.app.notification.domain.repository.NotificationRepository

class NotificationInteractor(
    private val notificationRepository: NotificationRepository
) {
    suspend fun isNotificationsEnabled(): Boolean =
        notificationRepository.isNotificationsEnabled()

    suspend fun setNotificationsEnabled(enabled: Boolean): Unit =
        notificationRepository.setNotificationsEnabled(enabled)

    suspend fun getNotificationTimestamp(key: String): Long =
        notificationRepository.getNotificationTimestamp(key)

    suspend fun setNotificationTimestamp(key: String, timestamp: Long): Unit =
        notificationRepository.setNotificationTimestamp(key, timestamp)
}