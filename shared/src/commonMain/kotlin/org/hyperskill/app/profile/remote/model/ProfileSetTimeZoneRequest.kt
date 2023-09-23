package org.hyperskill.app.profile.remote.model

import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSetTimeZoneRequest(
    @SerialName("timezone")
    val timeZone: TimeZone
)
