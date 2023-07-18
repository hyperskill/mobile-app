package org.hyperskill.app.learning_activities.domain.model

import kotlin.math.floor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LearningActivity(
    @SerialName("id")
    val id: Long,
    @SerialName("state")
    private val stateValue: Int,
    @SerialName("target_id")
    val targetId: Long?,
    @SerialName("target_type")
    private val targetTypeValue: String?,
    @SerialName("type")
    internal val typeValue: Int,
    @SerialName("title")
    val title: String = "",
    @SerialName("description")
    val description: String? = null,
    @SerialName("hypercoins_award")
    val hypercoinsAward: Int = 0,
    @SerialName("is_ide_required")
    val isIdeRequired: Boolean = false,
    @SerialName("progress")
    val progress: Float = 0f
) {
    val state: LearningActivityState?
        get() = LearningActivityState.getByValue(stateValue)

    val type: LearningActivityType?
        get() = LearningActivityType.getByValue(typeValue)

    val targetType: LearningActivityTargetType?
        get() = targetTypeValue?.let { LearningActivityTargetType.getByValue(it) }

    val progressPercentage: Int
        get() = floor(progress * 100).toInt()
}