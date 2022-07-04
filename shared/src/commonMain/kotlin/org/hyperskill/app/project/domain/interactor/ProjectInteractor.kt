package org.hyperskill.app.project.domain.interactor

import org.hyperskill.app.project.domain.model.Project
import org.hyperskill.app.project.domain.repository.ProjectRepository

class ProjectInteractor(
    private val projectRepository: ProjectRepository
) {
    suspend fun getProject(projectId: Long): Result<Project> =
        kotlin.runCatching {
            return projectRepository.getProject(projectId)
        }

    suspend fun getProjects(projectIds: List<Long>): Result<List<Project>> =
        kotlin.runCatching {
            return projectRepository.getProjects(projectIds)
        }
}