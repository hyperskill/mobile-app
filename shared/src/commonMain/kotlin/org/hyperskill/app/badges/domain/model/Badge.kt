package org.hyperskill.app.badges.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Badge(
    @SerialName("id")
    val id: Long,
    @SerialName("kind")
    val kind: BadgeType,
    @SerialName("title")
    val title: String,
    @SerialName("level")
    val level: Int,
    @SerialName("value")
    val value: Int,
    @SerialName("current_level_value")
    val currentLevelValue: Int,
    @SerialName("next_level_value")
    val nextLevelValue: Int? = null,
    @SerialName("is_max_level")
    val isMaxLevel: Boolean
)