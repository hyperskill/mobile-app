package org.hyperskill.app.step_quiz.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttemptRequest(
    @SerialName("step")
    val step: Long
)