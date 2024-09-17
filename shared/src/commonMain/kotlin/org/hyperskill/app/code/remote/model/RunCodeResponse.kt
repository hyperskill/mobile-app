package org.hyperskill.app.code.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.code.domain.model.CodeExecutionResult
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse

@Serializable
data class RunCodeResponse(
    @SerialName("meta")
    override val meta: Meta,
    @SerialName("run-code")
    val codeExecutionResults: List<CodeExecutionResult>
) : MetaResponse