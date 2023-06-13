package org.hyperskill.app.devices.domain.repository

import org.hyperskill.app.devices.domain.model.Device

interface DevicesRepository {
    suspend fun createDevice(device: Device): Result<Device>

    fun getCurrentCachedDevice(): Result<Device>
}