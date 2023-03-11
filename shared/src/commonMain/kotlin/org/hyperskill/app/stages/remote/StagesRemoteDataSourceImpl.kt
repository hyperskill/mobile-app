package org.hyperskill.app.stages.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.stages.data.source.StagesRemoteDataSource
import org.hyperskill.app.stages.domain.model.Stage
import org.hyperskill.app.stages.remote.model.StagesResponse

class StagesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StagesRemoteDataSource {
    override suspend fun getStages(stagesIds: List<Long>): Result<List<Stage>> =
        kotlin.runCatching {
            httpClient
                .get("/api/stages") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", stagesIds.joinToString(separator = ","))
                }.body<StagesResponse>().stages
        }

    override suspend fun getProjectStages(projectId: Long): Result<List<Stage>> =
        kotlin.runCatching {
            httpClient
                .get("/api/stages") {
                    contentType(ContentType.Application.Json)
                    parameter("project", projectId)
                }.body<StagesResponse>().stages
        }
}