package org.hyperskill.app.projects.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.progresses.domain.model.Progress

@Serializable
data class ProjectProgress(
    @SerialName("id")
    val id: String,
    @SerialName("vid")
    val vid: String,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("clarity")
    override val clarity: Float?,
    @SerialName("fun")
    override val funMeasure: Float?,
    @SerialName("usefulness")
    override val usefulness: Float?,
    @SerialName("seconds_to_complete")
    val secondsToComplete: Float?,
    @SerialName("feature_score")
    val featureScore: Float = 0f,
    @SerialName("base_score")
    val baseScore: Float = 0f,
    @SerialName("completed_stages")
    val completedStages: List<Long>?
) : Progress {
    companion object
}

val ProjectProgress.projectId: Long?
    get() = vid.substringAfter("project-").toLongOrNull()