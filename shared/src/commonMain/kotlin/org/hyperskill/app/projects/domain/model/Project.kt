package org.hyperskill.app.projects.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("is_deprecated")
    val isDeprecated: Boolean = false,
    @SerialName("progress_id")
    val progressId: String,
    @SerialName("tracks")
    val tracks: Map<String, ProjectTracksEntry>,
    @SerialName("is_ide_required")
    val isIdeRequired: Boolean = false
) {
    companion object
}

fun Project.isGraduated(trackId: Long): Boolean =
    tracks[trackId.toString()]?.kind == ProjectKind.GRADUATE