package org.hyperskill.app.code.domain.repository

import org.hyperskill.app.code.domain.model.RunCodeResult
import org.hyperskill.app.code.remote.model.RunCodeRequest

interface CodeRepository {
    suspend fun runCode(runCodeRequest: RunCodeRequest): Result<RunCodeResult>
}