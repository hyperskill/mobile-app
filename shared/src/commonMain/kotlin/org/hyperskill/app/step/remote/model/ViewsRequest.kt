package org.hyperskill.app.step.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.step.domain.model.StepContext

@Serializable
data class ViewsRequest(
    @SerialName("step")
    val stepId: Long,
    @SerialName("context")
    val stepContext: StepContext
)