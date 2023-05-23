package org.hyperskill.app.step_quiz.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.network.remote.parameterIds
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step_quiz.data.source.SubmissionRemoteDataSource
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.remote.model.SubmissionRequest
import org.hyperskill.app.step_quiz.remote.model.SubmissionResponse

class SubmissionRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : SubmissionRemoteDataSource {
    override suspend fun getSubmissionsForStep(stepId: Long, userId: Long, page: Int): Result<List<Submission>> =
        kotlin.runCatching {
            httpClient
                .get("/api/submissions") {
                    contentType(ContentType.Application.Json)
                    parameter("step", stepId)
                    parameter("user", userId)
                    parameter("page", page)
                }.body<SubmissionResponse>().submissions
        }

    override suspend fun getSubmissions(submissionsIds: List<Long>): Result<List<Submission>> =
        kotlin.runCatching {
            httpClient
                .get("/api/submissions") {
                    contentType(ContentType.Application.Json)
                    parameterIds(submissionsIds)
                }.body<SubmissionResponse>().submissions
        }

    override suspend fun createSubmission(
        attemptId: Long,
        reply: Reply,
        solvingContext: StepContext
    ): Result<Submission> =
        kotlin.runCatching {
            httpClient
                .post("/api/submissions") {
                    contentType(ContentType.Application.Json)
                    setBody(SubmissionRequest(attemptId, reply, solvingContext))
                }.body<SubmissionResponse>().submissions.first()
        }
}