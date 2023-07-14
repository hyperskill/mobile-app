package org.hyperskill.app.devices.data.repository

import org.hyperskill.app.devices.data.source.CurrentDeviceCacheDataSource
import org.hyperskill.app.devices.data.source.DevicesRemoteDataSource
import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.domain.repository.DevicesRepository

class DevicesRepositoryImpl(
    private val devicesRemoteDataSource: DevicesRemoteDataSource,
    private val currentDeviceCacheDataSource: CurrentDeviceCacheDataSource
) : DevicesRepository {
    override suspend fun createDevice(device: Device): Result<Device> =
        devicesRemoteDataSource
            .createDevice(device)
            .onSuccess { createdDevice ->
                if (createdDevice.isActive) {
                    currentDeviceCacheDataSource.setCurrentDevice(createdDevice)
                }
            }

    override fun getCurrentCachedDevice(): Result<Device?> =
        currentDeviceCacheDataSource.getCurrentDevice()
}