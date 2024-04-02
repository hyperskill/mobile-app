package org.hyperskill.app.submissions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateCodeWithErrorsRequest(
    @SerialName("step")
    val stepId: Long
)