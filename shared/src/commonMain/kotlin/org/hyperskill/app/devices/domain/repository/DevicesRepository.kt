package org.hyperskill.app.devices.domain.repository

import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.domain.model.DeviceType

interface DevicesRepository {
    suspend fun createDevice(
        name: String? = null,
        registrationId: String,
        isActive: Boolean,
        type: DeviceType
    ): Result<Device>

    fun getCurrentCachedDevice(): Result<Device>
    fun saveDeviceToCache(device: Device)
    fun clearCache()
}