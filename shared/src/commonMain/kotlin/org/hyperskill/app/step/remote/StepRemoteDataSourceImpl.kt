package org.hyperskill.app.step.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.remote.model.StepResponse

class StepRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StepRemoteDataSource {
    override suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        kotlin.runCatching {
            httpClient
                .get<StepResponse>("/api/steps") {
                    header("Content-Type", "application/json")
                    parameter("ids", stepIds.joinToString(separator = ","))
                }
                .steps
        }
}