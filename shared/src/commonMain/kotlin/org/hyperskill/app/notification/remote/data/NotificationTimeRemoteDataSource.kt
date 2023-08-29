package org.hyperskill.app.notification.remote.data

interface NotificationTimeRemoteDataSource {
    /**
     * Sets the daily study reminder notification time.
     *
     * @param notificationHour The hour at which the notification should be shown.
     * If null, the reminder will be disabled.
     */
    suspend fun setDailyStudyReminderNotificationTime(notificationHour: Int?): Result<Unit>
}