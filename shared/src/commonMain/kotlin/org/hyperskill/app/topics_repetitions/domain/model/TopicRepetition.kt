package org.hyperskill.app.topics_repetitions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicRepetition(
    @SerialName("id")
    val id: Long,
    @SerialName("topic_id")
    val topicId: Long,
    @SerialName("steps")
    val steps: List<Long>,
    @SerialName("topic_title")
    val topicTitle: String,
    @SerialName("is_in_current_track")
    val isInCurrentTrack: Boolean,
    @SerialName("repeated_count")
    val repeatedCount: Int
)