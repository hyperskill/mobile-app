package org.hyperskill.app.run_code.data.source

import org.hyperskill.app.run_code.remote.model.RunCodeRequest
import org.hyperskill.app.run_code.remote.model.RunCodeResponse

interface RunCodeRemoteDataSource {
    suspend fun runCode(runCodeRequest: RunCodeRequest): Result<RunCodeResponse>
}