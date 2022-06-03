package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cell(
    @SerialName("name")
    val name: String,
    @SerialName("answer")
    var answer: Boolean
)
