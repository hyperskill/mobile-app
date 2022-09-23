package org.hyperskill.app.project.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectTrack(
    @SerialName("level")
    val level: String,
    @SerialName("kind")
    val kind: String
)
