package org.hyperskill.app.code.data.repository

import org.hyperskill.app.code.data.source.CodeRemoteDataSource
import org.hyperskill.app.code.domain.model.CodeExecutionResult
import org.hyperskill.app.code.domain.repository.CodeRepository
import org.hyperskill.app.code.remote.model.RunCodeRequest

internal class CodeRepositoryImpl(
    private val codeRemoteDataSource: CodeRemoteDataSource
) : CodeRepository {
    override suspend fun runCode(runCodeRequest: RunCodeRequest): Result<CodeExecutionResult> =
        codeRemoteDataSource
            .runCode(runCodeRequest)
            .map { it.codeExecutionResults.first() }
}