package org.hyperskill.app.notification.remote.data

import org.hyperskill.app.notification.remote.domain.repository.NotificationTimeRepository

class NotificationTimeRepositoryImpl(
    private val notificationTimeDataSource: NotificationTimeRemoteDataSource
) : NotificationTimeRepository {
    override suspend fun setDailyStudyReminderNotificationTime(notificationHour: Int): Result<Unit> =
        notificationTimeDataSource.setDailyStudyReminderNotificationTime(notificationHour)

    override suspend fun disableDailyStudyReminderNotification(): Result<Unit> =
        notificationTimeDataSource.setDailyStudyReminderNotificationTime(null)
}