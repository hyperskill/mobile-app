package org.hyperskill.app.notification.domain.repository

interface NotificationRepository {
    suspend fun isNotificationsEnabled(): Boolean

    suspend fun setNotificationsEnabled(enabled: Boolean)

    suspend fun getNotificationTimestamp(key: String): Long

    suspend fun setNotificationTimestamp(key: String, timestamp: Long)
}