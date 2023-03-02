package org.hyperskill.app.notification.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.SharedResources.strings
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.notification.data.extension.NotificationExtensions
import org.hyperskill.app.notification.data.model.NotificationDescription
import org.hyperskill.app.notification.data.source.NotificationCacheDataSource

class NotificationCacheDataSourceImpl(
    private val settings: Settings,
    private val resourceProvider: ResourceProvider
) : NotificationCacheDataSource {
    override fun isNotificationsPermissionGranted(): Boolean =
        settings.getBoolean(NotificationCacheKeyValues.NOTIFICATIONS_PERMISSION_GRANTED, false)

    override fun setNotificationsPermissionGranted(isGranted: Boolean) {
        settings.putBoolean(NotificationCacheKeyValues.NOTIFICATIONS_PERMISSION_GRANTED, isGranted)
    }

    override fun isDailyStudyRemindersEnabled(): Boolean =
        settings.getBoolean(NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_ENABLED, false)

    override fun setDailyStudyRemindersEnabled(isEnabled: Boolean) {
        settings.putBoolean(NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_ENABLED, isEnabled)
    }

    override fun getNotificationTimestamp(key: String): Long =
        settings.getLong(key, 0)

    override fun setNotificationTimestamp(key: String, timestamp: Long) {
        settings.putLong(key, timestamp)
    }

    override fun getDailyStudyRemindersIntervalStartHour(): Int =
        settings.getInt(
            NotificationCacheKeyValues.NOTIFICATIONS_DAILY_REMINDER_START_HOUR,
            NotificationExtensions.DAILY_REMINDERS_AFTER_STEP_SOLVED_START_HOUR
        )

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
                1,
                resourceProvider.getString(strings.notification1_title),
                resourceProvider.getString(strings.notification1_text)
            ),
            NotificationDescription(
                2,
                resourceProvider.getString(strings.notification2_title),
                resourceProvider.getString(strings.notification2_text)
            ),
            NotificationDescription(
                3,
                resourceProvider.getString(strings.notification3_title),
                resourceProvider.getString(strings.notification3_text)
            ),
            NotificationDescription(
                4,
                resourceProvider.getString(strings.notification4_title),
                resourceProvider.getString(strings.notification4_text)
            ),
            NotificationDescription(
                5,
                resourceProvider.getString(strings.notification5_title),
                resourceProvider.getString(strings.notification5_text)
            ),
            NotificationDescription(
                6,
                resourceProvider.getString(strings.notification6_title),
                resourceProvider.getString(strings.notification6_text)
            ),
            NotificationDescription(
                7,
                resourceProvider.getString(strings.notification7_title),
                resourceProvider.getString(strings.notification7_text)
            ),
            NotificationDescription(
                8,
                resourceProvider.getString(strings.notification8_title),
                resourceProvider.getString(strings.notification8_text)
            ),
            NotificationDescription(
                9,
                resourceProvider.getString(strings.notification9_title),
                resourceProvider.getString(strings.notification9_text)
            ),
            NotificationDescription(
                10,
                resourceProvider.getString(strings.notification10_title),
                resourceProvider.getString(strings.notification10_text)
            ),
            NotificationDescription(
                11,
                resourceProvider.getString(strings.notification11_title),
                resourceProvider.getString(strings.notification11_text)
            ),
            NotificationDescription(
                12,
                resourceProvider.getString(strings.notification12_title),
                resourceProvider.getString(strings.notification12_text)
            ),
            NotificationDescription(
                13,
                resourceProvider.getString(strings.notification13_title),
                resourceProvider.getString(strings.notification13_text)
            ),
            NotificationDescription(
                14,
                resourceProvider.getString(strings.notification14_title),
                resourceProvider.getString(strings.notification14_text)
            )
        )

    override fun getLastTimeUserAskedToEnableDailyReminders(): Long? =
        settings.getLongOrNull(NotificationCacheKeyValues.NOTIFICATION_LAST_TIME_USER_ASKED_TO_ENABLE_DAILY_REMINDERS)

    override fun setLastTimeUserAskedToEnableDailyReminders(timestamp: Long) {
        settings.putLong(
            NotificationCacheKeyValues.NOTIFICATION_LAST_TIME_USER_ASKED_TO_ENABLE_DAILY_REMINDERS,
            timestamp
        )
        settings.putLong(
            NotificationCacheKeyValues.NOTIFICATION_USER_ASKED_TO_ENABLE_DAILY_REMINDERS_COUNT,
            getUserAskedToEnableDailyRemindersCount() + 1
        )
    }

    override fun getUserAskedToEnableDailyRemindersCount(): Long =
        settings.getLong(NotificationCacheKeyValues.NOTIFICATION_USER_ASKED_TO_ENABLE_DAILY_REMINDERS_COUNT, 0)
}