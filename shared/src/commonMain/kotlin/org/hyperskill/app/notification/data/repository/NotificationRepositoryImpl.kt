package org.hyperskill.app.notification.data.repository

import org.hyperskill.app.notification.data.model.NotificationDescription
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource
import org.hyperskill.app.notification.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val notificationCacheDataSource: NotificationCacheDataSource
) : NotificationRepository {
    override fun isDailyStudyRemindersEnabled(): Boolean =
        notificationCacheDataSource.isDailyStudyRemindersEnabled()

    override fun setDailyStudyRemindersEnabled(enabled: Boolean) {
        notificationCacheDataSource.setDailyStudyRemindersEnabled(enabled)
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

    override fun getRandomDailyStudyRemindersNotificationDescription(): NotificationDescription =
        notificationCacheDataSource.getRandomDailyStudyRemindersNotificationDescription()

    override fun getLastTimeUserAskedToEnableDailyReminders(): Long? =
        notificationCacheDataSource.getLastTimeUserAskedToEnableDailyReminders()

    override fun setLastTimeUserAskedToEnableDailyReminders(timestamp: Long) {
        notificationCacheDataSource.setLastTimeUserAskedToEnableDailyReminders(timestamp)
    }

    override fun getUserAskedToEnableDailyRemindersCount(): Long =
        notificationCacheDataSource.getUserAskedToEnableDailyRemindersCount()

    override fun getShuffledDailyStudyRemindersNotificationDescriptions(): List<NotificationDescription> =
        notificationCacheDataSource.getShuffledDailyStudyRemindersNotificationDescriptions()

    override fun clearAskUserToEnableDailyRemindersInfo() {
        notificationCacheDataSource.clearAskUserToEnableDailyRemindersInfo()
    }
}