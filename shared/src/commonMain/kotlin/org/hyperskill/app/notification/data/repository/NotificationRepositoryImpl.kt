package org.hyperskill.app.notification.data.repository

import org.hyperskill.app.notification.data.model.NotificationDescription
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

    override fun getDailyStudyRemindersIntervalStartHour(): Int =
        notificationCacheDataSource.getDailyStudyRemindersIntervalStartHour()

    override fun setDailyStudyRemindersIntervalStartHour(hour: Int) {
        notificationCacheDataSource.setDailyStudyRemindersIntervalStartHour(hour)
    }

    override fun getRandomNotificationDescription(): NotificationDescription =
        notificationCacheDataSource.getRandomNotificationDescription()
}