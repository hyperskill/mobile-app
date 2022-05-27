package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feedback(
    @SerialName("blanks_feedback")
    val blanksFeedback: List<Boolean>? = null,
    @SerialName("options_feedback")
    val optionsFeedback: List<String>? = null,
    val stringFeedback: String? = null
)