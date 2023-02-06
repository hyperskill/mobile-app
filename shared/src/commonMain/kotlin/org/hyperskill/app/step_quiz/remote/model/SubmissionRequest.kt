package org.hyperskill.app.step_quiz.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply

@Serializable
data class SubmissionRequest(
    @SerialName("attempt")
    val attempt: Long,
    @SerialName("reply")
    val reply: Reply,
    @SerialName("solving_context")
    val solvingContext: StepContext
)