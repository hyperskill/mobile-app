package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubmissionStatus {
    @SerialName("evaluation")
    EVALUATION,
    @SerialName("wrong")
    WRONG,
    @SerialName("correct")
    CORRECT,
    @SerialName("outdated")
    OUTDATED,
    @SerialName("local")
    LOCAL
}