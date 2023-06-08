package org.hyperskill.app.devices.data.repository

import org.hyperskill.app.devices.data.source.DevicesCacheDataSource
import org.hyperskill.app.devices.data.source.DevicesRemoteDataSource
import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.domain.model.DeviceType
import org.hyperskill.app.devices.domain.model.backendName
import org.hyperskill.app.devices.domain.repository.DevicesRepository

class DevicesRepositoryImpl(
    private val devicesCacheDataSource: DevicesCacheDataSource,
    private val devicesRemoteDataSource: DevicesRemoteDataSource
) : DevicesRepository {
    override suspend fun createDevice(
        name: String?,
        registrationId: String,
        isActive: Boolean,
        type: DeviceType
    ): Result<Device> =
        devicesRemoteDataSource.createDevice(
            name = name,
            registrationId = registrationId,
            isActive = isActive,
            type = type.backendName
        )

    override fun getCurrentCachedDevice(): Result<Device> =
        devicesCacheDataSource.getCurrentDevice()

    override fun saveDeviceToCache(device: Device) {
        devicesCacheDataSource.saveDevice(device)
    }

    override fun clearCache() {
        devicesCacheDataSource.clearCache()
    }
}