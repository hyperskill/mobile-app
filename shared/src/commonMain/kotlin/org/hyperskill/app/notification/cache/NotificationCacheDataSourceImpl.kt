package org.hyperskill.app.notification.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.SharedResources.strings
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.notification.data.model.NotificationDescription
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource

class NotificationCacheDataSourceImpl(
    private val settings: Settings,
    private val resourceProvider: ResourceProvider
) : NotificationCacheDataSource {

    override fun isDailyStudyRemindersEnabled(): Boolean =
        settings.getBoolean(NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_ENABLED, false)

    override fun setDailyStudyRemindersEnabled(enabled: Boolean) {
        settings.putBoolean(NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_ENABLED, enabled)
    }

    override fun getNotificationTimestamp(key: String): Long =
        settings.getLong(key, 0)

    override fun setNotificationTimestamp(key: String, timestamp: Long) {
        settings.putLong(key, timestamp)
    }

    override fun getDailyStudyRemindersIntervalStartHour(): Int =
        settings.getInt(NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_START_HOUR, NotificationCacheKeyValues.NOTIFICATION_DAILY_REMINDER_DEFAULT_START_HOUR)

    override fun setDailyStudyRemindersIntervalStartHour(hour: Int) {
        settings.putInt(NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_START_HOUR, hour)
    }

    override fun getRandomDailyStudyRemindersNotificationDescription(): NotificationDescription =
        getDailyStudyRemindersNotificationDescriptions().random()

    override fun getShuffledDailyStudyRemindersNotificationDescriptions(): List<NotificationDescription> =
        getDailyStudyRemindersNotificationDescriptions().shuffled()

    private fun getDailyStudyRemindersNotificationDescriptions() =
        listOf(
            NotificationDescription(
                resourceProvider.getString(strings.notification1_title),
                resourceProvider.getString(strings.notification1_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification2_title),
                resourceProvider.getString(strings.notification2_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification3_title),
                resourceProvider.getString(strings.notification3_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification4_title),
                resourceProvider.getString(strings.notification4_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification5_title),
                resourceProvider.getString(strings.notification5_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification6_title),
                resourceProvider.getString(strings.notification6_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification7_title),
                resourceProvider.getString(strings.notification7_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification8_title),
                resourceProvider.getString(strings.notification8_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification9_title),
                resourceProvider.getString(strings.notification9_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification10_title),
                resourceProvider.getString(strings.notification10_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification11_title),
                resourceProvider.getString(strings.notification11_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification12_title),
                resourceProvider.getString(strings.notification12_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification13_title),
                resourceProvider.getString(strings.notification13_text)
            ),
            NotificationDescription(
                resourceProvider.getString(strings.notification14_title),
                resourceProvider.getString(strings.notification14_text)
            ),
        )
}