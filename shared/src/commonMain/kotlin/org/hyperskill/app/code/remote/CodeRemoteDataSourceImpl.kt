package org.hyperskill.app.code.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.code.data.source.CodeRemoteDataSource
import org.hyperskill.app.code.domain.model.RunCodeResult
import org.hyperskill.app.code.remote.model.RunCodeRequest

internal class CodeRemoteDataSourceImpl(private val httpClient: HttpClient) : CodeRemoteDataSource {
    override suspend fun runCode(runCodeRequest: RunCodeRequest): Result<RunCodeResult> =
        runCatching {
            httpClient
                .post("/api/run-code") {
                    contentType(ContentType.Application.Json)
                    setBody(runCodeRequest)
                }
                .body()
        }
}