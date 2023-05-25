package org.hyperskill.app.projects.domain.repository

import org.hyperskill.app.projects.domain.model.Project

interface ProjectsRepository {
    suspend fun getProjects(projectsIds: List<Long>, forceLoadFromRemote: Boolean): Result<List<Project>>

    suspend fun getProject(projectId: Long, forceLoadFromRemote: Boolean): Result<Project> =
        kotlin.runCatching {
            getProjects(listOf(projectId), forceLoadFromRemote).getOrThrow().first()
        }

    fun clearCache()
}