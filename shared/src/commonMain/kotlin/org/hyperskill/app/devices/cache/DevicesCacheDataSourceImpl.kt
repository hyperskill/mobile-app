package org.hyperskill.app.devices.cache

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.hyperskill.app.devices.data.source.DevicesCacheDataSource
import org.hyperskill.app.devices.domain.model.Device

class DevicesCacheDataSourceImpl(
    private val json: Json,
    private val settings: Settings
) : DevicesCacheDataSource {
    override fun getCurrentDevice(): Result<Device> =
        kotlin.runCatching {
            json.decodeFromString(
                Device.serializer(),
                settings.getString(DevicesCacheKeyValues.CURRENT_DEVICE, defaultValue = "")
            )
        }

    override fun saveDevice(device: Device) {
        settings.putString(
            key = DevicesCacheKeyValues.CURRENT_DEVICE,
            value = json.encodeToString(Device.serializer(), device)
        )
    }
}