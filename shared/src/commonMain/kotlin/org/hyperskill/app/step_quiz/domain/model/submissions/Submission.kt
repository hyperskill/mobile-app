package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Submission(
    @SerialName("id")
    val id: Long = 0,
    @SerialName("status")
    val status: SubmissionStatus? = null,
    @SerialName("score")
    val score: String? = null,
    @SerialName("hint")
    val hint: String? = null,
    @SerialName("time")
    val time: String? = null,
    @SerialName("reply")
    val reply: Reply? = null,
    @SerialName("attempt")
    val attempt: Long = 0,
    @SerialName("feedback")
    val feedback: Feedback? = null
)
