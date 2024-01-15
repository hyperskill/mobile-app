package org.hyperskill.app.profile_settings.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.profile_settings.cache.ProfileSettingsCacheDataSourceImpl

/**
 * Represents profile settings.
 *
 * Warning!
 * This model is stored in the cache.
 * Adding new field or modifying old ones,
 * check that all fields will be deserialized from cache without an error.
 * All the new optional fields must have default values.
 * @see [ProfileSettingsCacheDataSourceImpl]
 */
@Serializable
data class ProfileSettings(
    @SerialName("theme")
    val theme: Theme = Theme.SYSTEM
)