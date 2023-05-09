package org.hyperskill.app.track.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectsByLevel(
    @SerialName("nightmare")
    val nightmare: List<Long>? = null,
    @SerialName("medium")
    val medium: List<Long>? = null,
    @SerialName("easy")
    val easy: List<Long>? = null,
    @SerialName("hard")
    val hard: List<Long>? = null
)