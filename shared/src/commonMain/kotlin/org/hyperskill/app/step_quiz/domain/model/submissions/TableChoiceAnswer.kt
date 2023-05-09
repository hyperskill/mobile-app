package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TableChoiceAnswer(
    @SerialName("name_row")
    val nameRow: String,
    @SerialName("columns")
    val columns: List<Cell>
)