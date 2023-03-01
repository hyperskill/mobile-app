package org.hyperskill.app.projects.domain.interactor

import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.projects.domain.repository.getProject

class ProjectsInteractor(
    private val projectsRepository: ProjectsRepository
) {
    suspend fun getProject(projectId: Long): Result<Project> =
        projectsRepository.getProject(projectId)

    suspend fun getProjects(projectsIds: List<Long>): Result<List<Project>> =
        projectsRepository.getProjects(projectsIds)
}