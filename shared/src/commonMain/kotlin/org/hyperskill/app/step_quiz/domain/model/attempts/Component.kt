package org.hyperskill.app.step_quiz.domain.model.attempts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Component(
    @SerialName("type")
    val type: String,
    @SerialName("text")
    val text: String?,
    @SerialName("options")
    val options: List<String>?
)