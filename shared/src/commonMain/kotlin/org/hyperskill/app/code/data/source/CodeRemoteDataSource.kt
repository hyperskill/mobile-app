package org.hyperskill.app.code.data.source

import org.hyperskill.app.code.domain.model.RunCodeResult
import org.hyperskill.app.code.remote.model.RunCodeRequest

interface CodeRemoteDataSource {
    suspend fun runCode(runCodeRequest: RunCodeRequest): Result<RunCodeResult>
}