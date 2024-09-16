package org.hyperskill.app.code.domain.repository

import org.hyperskill.app.code.domain.model.CodeExecutionResult
import org.hyperskill.app.code.remote.model.RunCodeRequest

interface CodeRepository {
    suspend fun runCode(runCodeRequest: RunCodeRequest): Result<CodeExecutionResult>
}