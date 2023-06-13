package org.hyperskill.app.devices.data.repository

import org.hyperskill.app.devices.data.source.DevicesCacheDataSource
import org.hyperskill.app.devices.data.source.DevicesRemoteDataSource
import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.domain.repository.DevicesRepository

class DevicesRepositoryImpl(
    private val devicesCacheDataSource: DevicesCacheDataSource,
    private val devicesRemoteDataSource: DevicesRemoteDataSource
) : DevicesRepository {
    override suspend fun createDevice(device: Device): Result<Device> =
        devicesRemoteDataSource.createDevice(device)

    override fun getCurrentCachedDevice(): Result<Device> =
        devicesCacheDataSource.getCurrentDevice()

    override fun saveDeviceToCache(device: Device) {
        devicesCacheDataSource.saveDevice(device)
    }
}