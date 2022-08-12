package org.hyperskill.app.notification.domain.repository

import org.hyperskill.app.notification.data.model.NotificationDescription

interface NotificationRepository {
    fun isNotificationsEnabled(): Boolean

    fun setNotificationsEnabled(enabled: Boolean)

    fun getNotificationTimestamp(key: String): Long

    fun setNotificationTimestamp(key: String, timestamp: Long)

    fun getRandomNotification(): NotificationDescription
}