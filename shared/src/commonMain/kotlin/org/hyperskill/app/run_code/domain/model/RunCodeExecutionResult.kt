package org.hyperskill.app.run_code.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RunCodeExecutionResult(
    @SerialName("language")
    val language: String,
    @SerialName("stdin")
    val stdin: String?,
    @SerialName("is_success")
    val isSuccess: Boolean,
    @SerialName("stdout")
    val stdout: String? = null,
    @SerialName("stderr")
    val stderr: String? = null
)