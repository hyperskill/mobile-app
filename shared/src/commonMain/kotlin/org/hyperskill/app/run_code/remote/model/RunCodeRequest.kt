package org.hyperskill.app.run_code.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RunCodeRequest(
    @SerialName("code")
    val code: String,
    @SerialName("language")
    val language: String,
    @SerialName("stdin")
    val stdin: String
)