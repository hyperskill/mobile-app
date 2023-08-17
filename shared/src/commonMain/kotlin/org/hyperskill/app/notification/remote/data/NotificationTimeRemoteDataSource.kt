package org.hyperskill.app.notification.remote.data

interface NotificationTimeRemoteDataSource {
    suspend fun setDailyStudyReminderNotificationTime(notificationHour: Int): Result<Unit>
}