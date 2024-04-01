package org.hyperskill.app.submissions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.submissions.domain.model.Reply

@Serializable
data class CreateSubmissionRequest(
    @SerialName("attempt")
    val attempt: Long,
    @SerialName("reply")
    val reply: Reply,
    @SerialName("solving_context")
    val solvingContext: StepContext
)