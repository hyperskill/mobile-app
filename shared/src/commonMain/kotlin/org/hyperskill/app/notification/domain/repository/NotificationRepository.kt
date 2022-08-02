package org.hyperskill.app.notification.domain.repository

interface NotificationRepository {
    suspend fun getNotificationsEnabled(): Boolean
    suspend fun getNotificationsTimestamp(): Int
}