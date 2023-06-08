package org.hyperskill.app.devices.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DevicesRequest(
    @SerialName("registration_id")
    val registrationId: String,
    @SerialName("active")
    val isActive: Boolean,
    @SerialName("type")
    val type: String
)