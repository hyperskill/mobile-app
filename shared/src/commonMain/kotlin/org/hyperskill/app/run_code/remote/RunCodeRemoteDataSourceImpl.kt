package org.hyperskill.app.run_code.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.run_code.data.source.RunCodeRemoteDataSource
import org.hyperskill.app.run_code.remote.model.RunCodeRequest
import org.hyperskill.app.run_code.remote.model.RunCodeResponse

internal class RunCodeRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : RunCodeRemoteDataSource {
    override suspend fun runCode(runCodeRequest: RunCodeRequest): Result<RunCodeResponse> =
        runCatching {
            httpClient
                .post("/api/run-code") {
                    contentType(ContentType.Application.Json)
                    setBody(runCodeRequest)
                }
                .body()
        }
}