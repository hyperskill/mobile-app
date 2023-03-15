package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LearningActivity(
    @SerialName("id")
    val id: Long,
    @SerialName("state")
    val state: TargetState,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("type")
    val type: TargetType
)