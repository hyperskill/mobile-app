package org.hyperskill.app.project.remote.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.project.data.source.ProjectRemoteDataSource
import org.hyperskill.app.project.domain.model.Project

class ProjectRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ProjectRemoteDataSource {
    override suspend fun getProjects(projectIds: List<Long>): Result<List<Project>> =
        kotlin.runCatching {
            httpClient
                .get("/api/projects") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", projectIds.joinToString(separator = ","))
                }.body<ProjectResponse>().projects
        }
}