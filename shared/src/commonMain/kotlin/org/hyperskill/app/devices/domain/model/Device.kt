package org.hyperskill.app.devices.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.devices.cache.CurrentDeviceCacheDataSourceImpl

/**
 * Represents a user's device.
 *
 * Warning!
 * This model is stored in the cache.
 * Adding new field or modifying old ones,
 * check that all fields will be deserialized from cache without an error.
 * All the new optional fields must have default values.
 * @see [CurrentDeviceCacheDataSourceImpl]
 */
@Serializable
data class Device(
    @SerialName("name")
    val name: String?,
    @SerialName("registration_id")
    val registrationId: String,
    @SerialName("active")
    val isActive: Boolean,
    @SerialName("type")
    val type: DeviceType = DeviceType.UNKNOWN
)