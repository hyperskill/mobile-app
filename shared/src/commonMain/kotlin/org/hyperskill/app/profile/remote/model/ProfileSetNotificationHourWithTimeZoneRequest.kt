package org.hyperskill.app.profile.remote.model

import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ProfileSetNotificationHourWithTimeZoneRequest(
    @SerialName("notification_hour")
    val notificationHour: Int,
    @SerialName("timezone")
    val timeZone: TimeZone
)