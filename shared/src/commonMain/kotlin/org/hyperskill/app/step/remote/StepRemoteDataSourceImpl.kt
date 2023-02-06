package org.hyperskill.app.step.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.remote.model.StepResponse

class StepRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StepRemoteDataSource {
    override suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        kotlin.runCatching {
            httpClient
                .get("/api/steps") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", stepIds.joinToString(separator = ","))
                }.body<StepResponse>().steps
        }

    override suspend fun completeStep(stepId: Long): Result<Step> =
        kotlin.runCatching {
            httpClient
                .post("/api/steps/$stepId/complete") {
                    contentType(ContentType.Application.Json)
                }
                .body<StepResponse>().steps.first()
        }

    override suspend fun skipStep(stepId: Long): Result<Step> =
        kotlin.runCatching {
            httpClient
                .post("/api/steps/$stepId/skip") {
                    contentType(ContentType.Application.Json)
                }
                .body<StepResponse>().steps.first()
        }

    override suspend fun viewStep(stepId: Long, stepContext: StepContext) {
        kotlin.runCatching {
            httpClient
                .post("/api/views") {
                    contentType(ContentType.Application.Json)
                    parameter("step", stepId)
                    parameter("context", stepContext)
                }
        }
    }

    override suspend fun getRecommendedStepsByTopicId(topicId: Long): Result<List<Step>> =
        kotlin.runCatching {
            httpClient
                .get("/api/steps") {
                    contentType(ContentType.Application.Json)
                    parameter("topic", topicId)
                    parameter("is_recommended", true)
                }.body<StepResponse>().steps
        }
}