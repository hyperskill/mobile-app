package org.hyperskill.app.projects.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.isGraduated
import org.hyperskill.app.projects.presentation.ProjectsListFeature
import org.hyperskill.app.projects.presentation.projectsByLevel
import org.hyperskill.app.projects.presentation.recommendedProjects
import org.hyperskill.app.projects.presentation.selectedProject

class ProjectsListViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: ProjectsListFeature.ContentState): ProjectsListFeature.ViewState =
        when (state) {
            ProjectsListFeature.ContentState.Idle -> ProjectsListFeature.ViewState.Idle
            ProjectsListFeature.ContentState.Loading -> ProjectsListFeature.ViewState.Loading
            is ProjectsListFeature.ContentState.Content -> ProjectsListFeature.ViewState.Content(
                formattedTitle = resourceProvider.getString(
                    SharedResources.strings.projects_list_title,
                    state.track.title
                ),
                selectedProject = state.selectedProject?.let {
                    mapProjectListItem(it, state.track.id)
                                                             },
                recommendedProjects = state.recommendedProjects.map {
                    mapProjectListItem(it, state.track.id)
                },
                projectsByLevel = state.projectsByLevel.mapValues { (_, projects) ->
                    projects.map { project ->
                        mapProjectListItem(project, state.track.id)
                    }
                },
                isRefreshing = state.isRefreshing
            )
            ProjectsListFeature.ContentState.Error -> ProjectsListFeature.ViewState.Error
        }

    private fun mapProjectListItem(
        project: Project,
        trackId: Long
    ): ProjectsListFeature.ProjectListItem =
        ProjectsListFeature.ProjectListItem(
            id = project.id,
            title = project.title,
            isGraduated = project.isGraduated(trackId)
        )
}