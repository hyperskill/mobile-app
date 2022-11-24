package org.hyperskill.app.topics.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Topic(
    @SerialName("id")
    val id: Long,
    @SerialName("progress_id")
    val progressId: String,
    @SerialName("theory")
    val theoryId: Long? = null,
    @SerialName("title")
    val title: String,
    @Transient
    val progress: TopicProgress? = null
)
