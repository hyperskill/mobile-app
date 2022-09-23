package org.hyperskill.app.project.data.repository

import org.hyperskill.app.project.data.source.ProjectRemoteDataSource
import org.hyperskill.app.project.domain.model.Project
import org.hyperskill.app.project.domain.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectRemoteDataSource: ProjectRemoteDataSource
) : ProjectRepository {
    override suspend fun getProjects(projectIds: List<Long>): Result<List<Project>> =
        projectRemoteDataSource.getProjects(projectIds)
}