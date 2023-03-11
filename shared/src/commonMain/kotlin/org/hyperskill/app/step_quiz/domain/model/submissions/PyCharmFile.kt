package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PyCharmFile(
    @SerialName("name")
    val name: String,
    @SerialName("is_visible")
    val isVisible: Boolean,
    @SerialName("text")
    val text: String
)
