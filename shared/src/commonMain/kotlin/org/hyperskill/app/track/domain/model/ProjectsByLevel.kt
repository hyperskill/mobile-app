package org.hyperskill.app.track.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectsByLevel(
    @SerialName("nightmare")
    val nightmare: List<Long>,
    @SerialName("medium")
    val medium: List<Long>,
    @SerialName("easy")
    val easy: List<Long>,
    @SerialName("hard")
    val hard: List<Long>
)
