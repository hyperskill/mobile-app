package org.hyperskill.app.projects.injection

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.projects.cache.ProjectsCacheDataSourceImpl
import org.hyperskill.app.projects.data.repository.ProjectsRepositoryImpl
import org.hyperskill.app.projects.data.source.ProjectsCacheDataSource
import org.hyperskill.app.projects.data.source.ProjectsRemoteDataSource
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.projects.remote.ProjectsRemoteDataSourceImpl

class ProjectsDataComponentImpl(appGraph: AppGraph) : ProjectsDataComponent {
    companion object {
        private val projectsCacheDataSource: ProjectsCacheDataSource by lazy {
            ProjectsCacheDataSourceImpl(InMemoryRepositoryCache())
        }
    }

    private val projectsRemoteDataSource: ProjectsRemoteDataSource =
        ProjectsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val projectsRepository: ProjectsRepository
        get() = ProjectsRepositoryImpl(projectsRemoteDataSource, projectsCacheDataSource)
}