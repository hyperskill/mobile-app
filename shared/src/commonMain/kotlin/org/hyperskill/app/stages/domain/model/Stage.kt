package org.hyperskill.app.stages.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stage(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("seconds_to_complete")
    val secondsToComplete: Float? = null,
    @SerialName("is_completed")
    val isCompleted: Boolean,

    @SerialName("project")
    val projectId: Long,
    @SerialName("project_stages_count")
    val projectStagesCount: Int,

    @SerialName("step")
    val stepId: Long,
    @SerialName("step_index")
    val stepIndex: Int
)