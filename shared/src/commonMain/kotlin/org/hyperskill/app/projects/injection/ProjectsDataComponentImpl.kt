package org.hyperskill.app.projects.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.projects.data.repository.ProjectsRepositoryImpl
import org.hyperskill.app.projects.data.source.ProjectsRemoteDataSource
import org.hyperskill.app.projects.domain.interactor.ProjectsInteractor
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.projects.remote.ProjectsRemoteDataSourceImpl

class ProjectsDataComponentImpl(appGraph: AppGraph) : ProjectsDataComponent {
    private val projectsRemoteDataSource: ProjectsRemoteDataSource =
        ProjectsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val projectsRepository: ProjectsRepository
        get() = ProjectsRepositoryImpl(projectsRemoteDataSource)

    override val projectsInteractor: ProjectsInteractor
        get() = ProjectsInteractor(projectsRepository)
}