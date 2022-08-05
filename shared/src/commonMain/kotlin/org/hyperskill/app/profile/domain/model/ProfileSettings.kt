package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSettings(
    @SerialName("theme")
    val theme: Theme = Theme.SYSTEM
)