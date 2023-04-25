package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyPlanSection(
    @SerialName("id")
    val id: Long,
    @SerialName("study_plan_id")
    val studyPlanId: Long,
    @SerialName("target_id")
    val targetId: Long? = null,
    @SerialName("target_type")
    val targetType: String? = null,
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
    val secondsToComplete: Float? = null,
    @SerialName("activities")
    val activities: List<Long>,
    // TODO: add section type field, using enum to detect root topics section
)