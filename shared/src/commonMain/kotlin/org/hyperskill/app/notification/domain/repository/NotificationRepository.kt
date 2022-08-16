package org.hyperskill.app.notification.domain.repository

import org.hyperskill.app.notification.data.model.NotificationDescription

interface NotificationRepository {
    fun isDailyStudyRemindersEnabled(): Boolean

    fun setDailyStudyRemindersEnabled(enabled: Boolean)

    fun getNotificationTimestamp(key: String): Long

    fun setNotificationTimestamp(key: String, timestamp: Long)

    fun getDailyStudyRemindersIntervalStartHour(): Int

    fun setDailyStudyRemindersIntervalStartHour(hour: Int)

    fun getRandomDailyStudyRemindersNotificationDescription(): NotificationDescription
}