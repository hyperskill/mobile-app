package org.hyperskill.app.submissions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GenerateCodeWithErrorsResponse(
    @SerialName("code")
    val code: String
)