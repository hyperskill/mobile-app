package org.hyperskill.app.devices.domain.model

import org.hyperskill.app.core.domain.platform.PlatformType

enum class DeviceType {
    IOS,
    ANDROID
}

internal val DeviceType.backendName: String
    get() = this.name.lowercase()

internal fun PlatformType.toDeviceType(): DeviceType =
    when (this) {
        PlatformType.IOS -> DeviceType.IOS
        PlatformType.ANDROID -> DeviceType.ANDROID
    }