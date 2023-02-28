package org.hyperskill.app.projects.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.projects.data.source.ProjectsRemoteDataSource
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.remote.model.ProjectsResponse

class ProjectsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ProjectsRemoteDataSource {
    override suspend fun getProjects(projectsIds: List<Long>): Result<List<Project>> =
        kotlin.runCatching {
            httpClient
                .get("/api/projects") {
                    contentType(ContentType.Application.Json)
                    parameter("ids", projectsIds.joinToString(separator = ","))
                }.body<ProjectsResponse>().projects
        }
}