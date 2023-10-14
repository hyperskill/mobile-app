package org.hyperskill.app.step_quiz.domain.model.attempts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Component(
    @SerialName("type")
    val type: Type,
    @SerialName("text")
    val text: String?,
    @SerialName("options")
    val options: List<String>?
) {
    @Serializable
    enum class Type {
        @SerialName("text")
        TEXT,
        @SerialName("input")
        INPUT,
        @SerialName("select")
        SELECT
    }
}