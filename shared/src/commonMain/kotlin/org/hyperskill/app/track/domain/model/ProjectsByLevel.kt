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

internal fun ProjectsByLevel.asLevelByProjectIdMap(): Map<Long, ProjectLevel> =
    buildMap {
        nightmare?.forEach {
            put(it, ProjectLevel.NIGHTMARE)
        }
        hard?.forEach {
            put(it, ProjectLevel.HARD)
        }
        medium?.forEach {
            put(it, ProjectLevel.MEDIUM)
        }
        easy?.forEach {
            put(it, ProjectLevel.EASY)
        }
    }