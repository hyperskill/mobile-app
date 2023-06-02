package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyPlanSection(
    @SerialName("id")
    val id: Long,
    @SerialName("type")
    private val typeValue: String,
    @SerialName("next_activity_id")
    val nextActivityId: Long? = null,
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
    val activities: List<Long>
) {
    val type: StudyPlanSectionType?
        get() = StudyPlanSectionType.getByValue(typeValue)
}