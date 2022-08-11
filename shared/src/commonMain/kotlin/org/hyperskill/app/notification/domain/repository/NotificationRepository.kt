package org.hyperskill.app.notification.domain.repository

interface NotificationRepository {
    fun isNotificationsEnabled(): Boolean

    fun setNotificationsEnabled(enabled: Boolean)

    fun getNotificationTimestamp(key: String): Long

    fun setNotificationTimestamp(key: String, timestamp: Long)

    fun getDailyStudyRemindersIntervalStartHour(): Int

    fun setDailyStudyRemindersIntervalStartHour(hour: Int)
}