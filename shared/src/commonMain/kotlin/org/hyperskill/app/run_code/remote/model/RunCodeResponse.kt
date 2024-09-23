package org.hyperskill.app.run_code.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.run_code.domain.model.RunCodeExecutionResult

@Serializable
data class RunCodeResponse(
    @SerialName("meta")
    override val meta: Meta,
    @SerialName("run-code")
    val runCodeExecutionResults: List<RunCodeExecutionResult>
) : MetaResponse