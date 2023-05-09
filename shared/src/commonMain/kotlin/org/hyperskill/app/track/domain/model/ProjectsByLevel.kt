package org.hyperskill.app.track.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.projects.domain.model.ProjectLevel

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

fun ProjectsByLevel.getProjectsIds(level: ProjectLevel): List<Long>? =
    when (level) {
        ProjectLevel.EASY -> easy
        ProjectLevel.MEDIUM -> medium
        ProjectLevel.HARD -> hard
        ProjectLevel.NIGHTMARE -> nightmare
    }