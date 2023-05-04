package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StudyPlanStatus {
    @SerialName("initing")
    INITING,

    @SerialName("updating")
    UPDATING,

    @SerialName("ready")
    READY
}