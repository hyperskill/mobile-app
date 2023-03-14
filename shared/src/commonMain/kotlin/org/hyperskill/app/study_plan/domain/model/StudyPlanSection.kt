package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class StudyPlanSection(
    @SerialName("id")
    val id: Long,
    @SerialName("study_plan_id")
    val studyPlanId: Long,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("target_type")
    val targetType: String,
    @SerialName("is_visible")
    val isVisible: Boolean,
    @SerialName("title")
    val title: String,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("topics_count")
    val topicsCount: Int,
    @SerialName("completed_topics_count")
    val completedTopicsCount: Int,
    @SerialName("seconds_to_complete")
    val secondsToComplete: Float
)