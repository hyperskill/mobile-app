package org.hyperskill.app.step_quiz.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.step_quiz.data.source.AttemptRemoteDataSource
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.remote.model.AttemptRequest
import org.hyperskill.app.step_quiz.remote.model.AttemptResponse

class AttemptRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : AttemptRemoteDataSource {
    override suspend fun getAttemptsForStep(stepId: Long, userId: Long): Result<List<Attempt>> =
        kotlin.runCatching {
            httpClient
                .get("/api/attempts") {
                    contentType(ContentType.Application.Json)
                    parameter("step", stepId)
                    parameter("user", userId)
                }.body<AttemptResponse>().attempts
        }

    override suspend fun createAttemptForStep(stepId: Long): Result<Attempt> =
        kotlin.runCatching {
            httpClient
                .post("/api/attempts") {
                    contentType(ContentType.Application.Json)
                    setBody(AttemptRequest(stepId))
                }.body<AttemptResponse>().attempts.first()
        }
}