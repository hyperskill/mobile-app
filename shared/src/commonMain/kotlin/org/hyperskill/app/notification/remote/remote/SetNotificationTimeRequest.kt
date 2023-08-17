package org.hyperskill.app.notification.remote.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetNotificationTimeRequest(
    @SerialName("notification_hour")
    val notificationHour: Int?
)