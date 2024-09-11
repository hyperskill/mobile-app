package org.hyperskill.app.code.data.source

import org.hyperskill.app.code.remote.model.RunCodeRequest
import org.hyperskill.app.code.remote.model.RunCodeResponse

interface CodeRemoteDataSource {
    suspend fun runCode(runCodeRequest: RunCodeRequest): Result<RunCodeResponse>
}