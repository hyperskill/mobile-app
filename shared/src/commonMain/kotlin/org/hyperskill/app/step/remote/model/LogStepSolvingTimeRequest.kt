package org.hyperskill.app.step.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogStepSolvingTimeRequest(
    @SerialName("step")
    val stepId: Long,
    @SerialName("duration")
    val seconds: Long
)