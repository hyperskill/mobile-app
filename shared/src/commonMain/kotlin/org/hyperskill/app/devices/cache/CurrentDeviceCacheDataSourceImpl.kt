package org.hyperskill.app.devices.cache

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.hyperskill.app.devices.data.source.CurrentDeviceCacheDataSource
import org.hyperskill.app.devices.domain.model.Device

class CurrentDeviceCacheDataSourceImpl(
    private val json: Json,
    private val settings: Settings
) : CurrentDeviceCacheDataSource {
    override fun getCurrentDevice(): Result<Device?> =
        kotlin.runCatching {
            settings
                .getStringOrNull(DevicesCacheKeyValues.CURRENT_DEVICE)
                ?.let { json.decodeFromString(Device.serializer(), it) }
        }

    override fun setCurrentDevice(device: Device) {
        settings.putString(
            key = DevicesCacheKeyValues.CURRENT_DEVICE,
            value = json.encodeToString(Device.serializer(), device)
        )
    }
}