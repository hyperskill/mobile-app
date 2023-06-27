package org.hyperskill.app.devices.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    @SerialName("name")
    val name: String?,
    @SerialName("registration_id")
    val registrationId: String,
    @SerialName("active")
    val isActive: Boolean,
    @SerialName("type")
    val type: DeviceType = DeviceType.UNKNOWN
)