package org.hyperskill.app.step_quiz.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

@Serializable
class SubmissionResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("submissions")
    val submissions: List<Submission>
) : MetaResponse