package org.hyperskill.app.devices.data.source

import org.hyperskill.app.devices.domain.model.Device

interface DevicesRemoteDataSource {
    suspend fun createDevice(
        name: String?,
        registrationId: String,
        isActive: Boolean,
        type: String
    ): Result<Device>
}