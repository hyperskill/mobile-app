package org.hyperskill.app.devices.data.source

import org.hyperskill.app.devices.domain.model.Device

interface DevicesCacheDataSource {
    fun getCurrentDevice(): Result<Device>
    fun saveDevice(device: Device)
}