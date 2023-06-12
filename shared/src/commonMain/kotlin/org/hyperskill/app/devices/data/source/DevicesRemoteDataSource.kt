package org.hyperskill.app.devices.data.source

import org.hyperskill.app.devices.domain.model.Device

interface DevicesRemoteDataSource {
    suspend fun createDevice(device: Device): Result<Device>
}