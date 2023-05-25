package org.hyperskill.app.projects.data.repository

import org.hyperskill.app.core.domain.repository_cache.RepositoryCacheProxy
import org.hyperskill.app.projects.data.source.ProjectsCacheDataSource
import org.hyperskill.app.projects.data.source.ProjectsRemoteDataSource
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectsRemoteDataSource: ProjectsRemoteDataSource,
    projectsCacheDataSource: ProjectsCacheDataSource
) : ProjectsRepository {
    private val projectsCacheProxy = RepositoryCacheProxy(
        cache = projectsCacheDataSource,
        loadValuesFromRemote = { projectsIds ->
            projectsRemoteDataSource.getProjects(projectsIds)
        },
        getKeyFromValue = { project ->
            project.id
        }
    )

    override suspend fun getProjects(
        projectsIds: List<Long>,
        forceLoadFromRemote: Boolean
    ): Result<List<Project>> =
        projectsCacheProxy.getValues(projectsIds, forceLoadFromRemote)

    override fun clearCache() {
        projectsCacheProxy.clearCache()
    }
}