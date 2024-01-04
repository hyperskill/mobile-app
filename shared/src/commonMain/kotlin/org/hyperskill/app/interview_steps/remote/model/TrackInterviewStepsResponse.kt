package org.hyperskill.app.interview_steps.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.interview_steps.domain.model.TrackInterviewStep

@Serializable
class TrackInterviewStepsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("steps")
    val steps: List<TrackInterviewStep>
) : MetaResponse