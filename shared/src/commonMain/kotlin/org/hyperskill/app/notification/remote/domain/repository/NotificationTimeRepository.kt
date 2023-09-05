package org.hyperskill.app.notification.remote.domain.repository

interface NotificationTimeRepository {
    /**
     * Sets the daily study reminder notification time
     * at which the daily study reminder notification should be triggered.
     *
     * @param notificationHour the hour of the day in 24-hour format (0-23) by UTC time zone
     * at which the notification should be triggered.
     */
    suspend fun setDailyStudyReminderNotificationTime(notificationHour: Int): Result<Unit>

    suspend fun disableDailyStudyReminderNotification(): Result<Unit>
}