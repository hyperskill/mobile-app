package org.hyperskill.app.projects.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("progress_id")
    val progressId: String,
    @SerialName("tracks")
    val tracks: Map<String, ProjectTracksEntry>,
    @SerialName("is_ide_required")
    val isIdeRequired: Boolean = false,
    @SerialName("default_score")
    val defaultScore: Float = 0f,
    @SerialName("results")
    val results: String = "", // HTML text
    @SerialName("provider_id")
    val providerId: Long? = null
) {
    companion object
}

fun Project.isGraduate(trackId: Long): Boolean =
    tracks[trackId.toString()]?.kind == ProjectKind.GRADUATE