package org.hyperskill.app.profile.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ProfileSetNotificationHourRequest(
    @SerialName("notification_hour")
    val notificationHour: Int?
)