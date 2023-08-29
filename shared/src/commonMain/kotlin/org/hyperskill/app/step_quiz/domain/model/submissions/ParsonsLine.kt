package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParsonsLine(
    @SerialName("level")
    val level: Int,
    @SerialName("line_number")
    val lineNumber: Int
)