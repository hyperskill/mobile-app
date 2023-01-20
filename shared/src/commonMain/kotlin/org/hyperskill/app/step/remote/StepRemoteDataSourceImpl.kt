package org.hyperskill.app.step.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.remote.model.StepResponse

class StepRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StepRemoteDataSource {
    override suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        kotlin.runCatching {
            httpClient
                .get("/api/steps") {
                    header("Content-Type", "application/json")
                    parameter("ids", stepIds.joinToString(separator = ","))
                }.body<StepResponse>().steps
        }

    override suspend fun completeStep(stepId: Long): Result<Step> =
        kotlin.runCatching {
            httpClient
                .post("/api/steps/$stepId/complete") {
                    header("Content-Type", "application/json")
                }
                .body<StepResponse>().steps.first()
        }

    override suspend fun skipStep(stepId: Long): Result<Step> =
        kotlin.runCatching {
            httpClient
                .post("/api/steps/$stepId/skip") {
                    header("Content-Type", "application/json")
                }
                .body<StepResponse>().steps.first()
        }

    override suspend fun getRecommendedStepsByTopicId(topicId: Long): Result<List<Step>> =
        kotlin.runCatching {
            httpClient
                .get("/api/steps") {
                    header("Content-Type", "application/json")
                    parameter("topic", topicId)
                    parameter("is_recommended", true)
                }.body<StepResponse>().steps
        }
}