package org.hyperskill.app.project.domain.repository

import org.hyperskill.app.project.domain.model.Project

interface ProjectRepository {
    suspend fun getProject(projectId: Long): Result<Project> =
        getProjects(listOf(projectId)).map { it.first() }

    suspend fun getProjects(projectIds: List<Long>): Result<List<Project>>
}