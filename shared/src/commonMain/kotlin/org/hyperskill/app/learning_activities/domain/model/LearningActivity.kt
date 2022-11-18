package org.hyperskill.app.learning_activities.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LearningActivity(
    @SerialName("id")
    val id: Long,
    @SerialName("state")
    val stateValue: Int,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("type")
    val typeValue: Int
) {
    val state: LearningActivityState?
        get() = LearningActivityState.getByValue(stateValue)

    val type: LearningActivityType?
        get() = LearningActivityType.getByValue(typeValue)
}
