package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    @SerialName("name")
    val name: String,
    @SerialName("size")
    val size: Long,
    @SerialName("url")
    val url: String,
    @SerialName("content")
    val content: String,
    @SerialName("type")
    val type: String
)
