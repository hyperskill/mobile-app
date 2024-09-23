package org.hyperskill.app.run_code.domain.repository

import org.hyperskill.app.run_code.domain.model.RunCodeExecutionResult
import org.hyperskill.app.run_code.remote.model.RunCodeRequest

interface RunCodeRepository {
    suspend fun runCode(runCodeRequest: RunCodeRequest): Result<RunCodeExecutionResult>
}