package org.hyperskill.app.step_quiz.domain.model.attempts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AttemptStatus {
    @SerialName("evaluation")
    EVALUATION,
    @SerialName("active")
    ACTIVE,
    @SerialName("outdated")
    OUTDATED
}