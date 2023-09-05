package org.hyperskill.app.notification.remote.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.notification.remote.data.NotificationTimeRemoteDataSource

class NotificationTimeRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : NotificationTimeRemoteDataSource {
    override suspend fun setDailyStudyReminderNotificationTime(notificationHour: Int?): Result<Unit> =
        runCatching {
            httpClient
                .post("/api/notifications/set-time") {
                    contentType(ContentType.Application.Json)
                    setBody(SetNotificationTimeRequest(notificationHour))
                }
        }
}