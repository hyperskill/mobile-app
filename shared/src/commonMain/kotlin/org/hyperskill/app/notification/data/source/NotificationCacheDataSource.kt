package org.hyperskill.app.notification.data.source

import org.hyperskill.app.notification.data.model.Notification

interface NotificationCacheDataSource {
    fun isNotificationsEnabled(): Boolean

    fun setNotificationsEnabled(enabled: Boolean)

    fun getNotificationTimestamp(key: String): Long

    fun setNotificationTimestamp(key: String, timestamp: Long)

    fun getRandomNotification(): Notification
}