package org.hyperskill.app.topics_repetitions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsRepetitions(
    @SerialName("repetitions")
    val repetitions: List<Repetition>,
    @SerialName("repetitions_by_count")
    val repetitionsByCount: Map<String, Int>,
)
