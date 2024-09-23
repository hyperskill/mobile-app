package org.hyperskill.app.run_code.data.repository

import org.hyperskill.app.run_code.data.source.RunCodeRemoteDataSource
import org.hyperskill.app.run_code.domain.model.RunCodeExecutionResult
import org.hyperskill.app.run_code.domain.repository.RunCodeRepository
import org.hyperskill.app.run_code.remote.model.RunCodeRequest

internal class RunCodeRepositoryImpl(
    private val runCodeRemoteDataSource: RunCodeRemoteDataSource
) : RunCodeRepository {
    override suspend fun runCode(runCodeRequest: RunCodeRequest): Result<RunCodeExecutionResult> =
        runCodeRemoteDataSource
            .runCode(runCodeRequest)
            .mapCatching { it.runCodeExecutionResults.first() }
}