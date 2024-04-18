package org.hyperskill.app.topics.domain.model

import kotlin.math.round
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicProgress(
    @SerialName("id")
    val id: String,
    @SerialName("vid")
    val vid: String,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("is_skipped")
    val isSkipped: Boolean,
    @SerialName("capacity")
    val capacity: Float? = null
)

val TopicProgress.topicId: Long?
    get() = vid.substringAfter("topic-").toLongOrNull()

val TopicProgress.completenessPercentage: Float
    get() = if (isCompleted || isSkipped) {
        100f
    } else {
        capacity?.let {
            round(capacity * 100)
        } ?: 0f
    }