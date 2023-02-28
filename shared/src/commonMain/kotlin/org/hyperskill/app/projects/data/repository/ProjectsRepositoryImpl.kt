package org.hyperskill.app.projects.data.repository

import org.hyperskill.app.projects.data.source.ProjectsRemoteDataSource
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectsRemoteDataSource: ProjectsRemoteDataSource
) : ProjectsRepository {
    override suspend fun getProjects(projectsIds: List<Long>): Result<List<Project>> =
        projectsRemoteDataSource.getProjects(projectsIds)
}