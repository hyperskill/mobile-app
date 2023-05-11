package org.hyperskill.app.projects.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectTracksEntry(
    @SerialName("level")
    val level: ProjectLevel,
    @SerialName("kind")
    val kind: ProjectKind
)