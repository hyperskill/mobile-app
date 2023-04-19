package org.hyperskill.app.learning_activities.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LearningActivity(
    @SerialName("id")
    val id: Long,
    @SerialName("state")
    private val stateValue: Int,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("type")
    private val typeValue: Int,
    @SerialName("is_current")
    val isCurrent: Boolean,
    @SerialName("title")
    val title: String = "",
    @SerialName("hypercoins_award")
    val hypercoinsAward: Int = 0
) {
    val state: LearningActivityState?
        get() = LearningActivityState.getByValue(stateValue)

    val type: LearningActivityType?
        get() = LearningActivityType.getByValue(typeValue)
}
