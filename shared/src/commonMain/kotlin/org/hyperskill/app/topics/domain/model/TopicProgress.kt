package org.hyperskill.app.topics.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicProgress(
    @SerialName("id")
    val id: String,
    @SerialName("stage_position")
    val stagePosition: Int? = null
)
