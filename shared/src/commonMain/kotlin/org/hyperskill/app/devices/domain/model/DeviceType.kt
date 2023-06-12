package org.hyperskill.app.devices.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.domain.platform.PlatformType

@Serializable
enum class DeviceType {
    @SerialName("ios")
    IOS,
    @SerialName("android")
    ANDROID,
    UNKNOWN
}

internal fun PlatformType.toDeviceType(): DeviceType =
    when (this) {
        PlatformType.IOS -> DeviceType.IOS
        PlatformType.ANDROID -> DeviceType.ANDROID
    }