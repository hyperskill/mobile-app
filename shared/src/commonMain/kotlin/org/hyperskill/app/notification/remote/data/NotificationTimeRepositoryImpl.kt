package org.hyperskill.app.notification.remote.data

import org.hyperskill.app.notification.remote.domain.repository.NotificationTimeRepository

class NotificationTimeRepositoryImpl(
    private val notificationTimeRemoteDataSource: NotificationTimeRemoteDataSource
) : NotificationTimeRepository {
    override suspend fun setDailyStudyReminderNotificationTime(notificationHour: Int): Result<Unit> =
        notificationTimeRemoteDataSource.setDailyStudyReminderNotificationTime(notificationHour)

    override suspend fun disableDailyStudyReminderNotification(): Result<Unit> =
        notificationTimeRemoteDataSource.setDailyStudyReminderNotificationTime(null)
}