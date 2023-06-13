package org.hyperskill.app.devices.data.source

import org.hyperskill.app.devices.domain.model.Device

interface CurrentDeviceCacheDataSource {
    fun getCurrentDevice(): Result<Device>
    fun setCurrentDevice(device: Device)
}