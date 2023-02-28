package org.hyperskill.app.projects.domain.repository

import org.hyperskill.app.projects.domain.model.Project

interface ProjectsRepository {
    suspend fun getProjects(projectsIds: List<Long>): Result<List<Project>>

    suspend fun getProject(projectId: Long): Result<Project> =
        kotlin.runCatching {
            getProjects(listOf(projectId)).getOrThrow().first()
        }
}