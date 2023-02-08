package org.hyperskill.app.topics.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.round

@Serializable
data class TopicProgress(
    @SerialName("id")
    val id: String,
    @SerialName("stage_position")
    val stagePosition: Int? = null,
    @SerialName("repeated_count")
    val repeatedCount: Int? = null,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("is_skipped")
    val isSkipped: Boolean,
    @SerialName("capacity")
    val capacity: Float? = null,
    @SerialName("is_in_current_track")
    val isInCurrentTrack: Boolean
)

val TopicProgress.completenessPercentage: Float
    get() = if (isCompleted || isSkipped) {
        100f
    } else {
        capacity?.let {
            round(capacity * 100)
        } ?: 0f
    }
