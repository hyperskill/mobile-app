package org.hyperskill.app.submissions.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.network.remote.parameterIds
import org.hyperskill.app.network.remote.parameterPage
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.submissions.data.source.SubmissionsRemoteDataSource
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.remote.model.CreateSubmissionRequest
import org.hyperskill.app.submissions.remote.model.SubmissionsResponse

internal class SubmissionsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : SubmissionsRemoteDataSource {
    override suspend fun getSubmissionsForStep(stepId: Long, userId: Long, page: Int): Result<List<Submission>> =
        kotlin.runCatching {
            httpClient
                .get("/api/submissions") {
                    contentType(ContentType.Application.Json)
                    parameter("step", stepId)
                    parameter("user", userId)
                    parameterPage(page)
                }.body<SubmissionsResponse>().submissions
        }

    override suspend fun getSubmissions(submissionsIds: List<Long>): Result<List<Submission>> =
        kotlin.runCatching {
            httpClient
                .get("/api/submissions") {
                    contentType(ContentType.Application.Json)
                    parameterIds(submissionsIds)
                }.body<SubmissionsResponse>().submissions
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
                    setBody(CreateSubmissionRequest(attemptId, reply, solvingContext))
                }.body<SubmissionsResponse>().submissions.first()
        }
}