package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StudyPlanSectionType {
    @SerialName("root topics")
    ROOT_TOPICS
}
