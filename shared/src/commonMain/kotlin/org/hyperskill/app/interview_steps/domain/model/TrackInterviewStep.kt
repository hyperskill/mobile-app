package org.hyperskill.app.interview_steps.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackInterviewStep(
    @SerialName("id")
    val id: Long
)