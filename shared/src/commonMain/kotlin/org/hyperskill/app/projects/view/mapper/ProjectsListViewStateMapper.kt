package org.hyperskill.app.projects.view.mapper

import kotlin.math.floor
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.progresses.domain.model.averageRating
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.isGraduated
import org.hyperskill.app.projects.presentation.ProjectsListFeature
import org.hyperskill.app.projects.presentation.bestRatedProjectId
import org.hyperskill.app.projects.presentation.projectsByLevel
import org.hyperskill.app.projects.presentation.recommendedProjects
import org.hyperskill.app.projects.presentation.selectedProject
import org.hyperskill.app.track.domain.model.getProjectLevel

internal class ProjectsListViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: ProjectsListFeature.ContentState): ProjectsListFeature.ViewState =
        when (state) {
            ProjectsListFeature.ContentState.Idle -> ProjectsListFeature.ViewState.Idle
            ProjectsListFeature.ContentState.Loading -> ProjectsListFeature.ViewState.Loading
            is ProjectsListFeature.ContentState.Content -> {
                val bestRatedProjectId = state.bestRatedProjectId
                ProjectsListFeature.ViewState.Content(
                    formattedTitle = resourceProvider.getString(
                        SharedResources.strings.projects_list_title,
                        state.track.title
                    ),
                    selectedProject = state.selectedProject?.let {
                        mapProjectListItem(
                            projectWithProgress = it,
                            trackId = state.track.id,
                            bestRatedProjectId = bestRatedProjectId,
                            level = state.track.projectsByLevel.getProjectLevel(it.project.id)
                        )
                    },
                    recommendedProjects = state.recommendedProjects.map {
                        mapProjectListItem(
                            projectWithProgress = it,
                            trackId = state.track.id,
                            bestRatedProjectId = bestRatedProjectId,
                            level = state.track.projectsByLevel.getProjectLevel(it.project.id)
                        )
                    },
                    projectsByLevel = state.projectsByLevel.mapValues { (level, projects) ->
                        projects.map { project ->
                            mapProjectListItem(
                                projectWithProgress = project,
                                trackId = state.track.id,
                                bestRatedProjectId = bestRatedProjectId,
                                level = level
                            )
                        }
                    },
                    isRefreshing = state.isRefreshing
                )
            }
            ProjectsListFeature.ContentState.Error -> ProjectsListFeature.ViewState.Error
        }

    private fun mapProjectListItem(
        projectWithProgress: ProjectWithProgress,
        trackId: Long,
        bestRatedProjectId: Long?,
        level: ProjectLevel?
    ): ProjectsListFeature.ProjectListItem =
        with(projectWithProgress) {
            ProjectsListFeature.ProjectListItem(
                id = project.id,
                title = project.title,
                averageRating = progress.averageRating(),
                level = level,
                timeToComplete = getTimeToComplete(progress.secondsToComplete),
                isGraduated = project.isGraduated(trackId),
                isBestRated = project.id == bestRatedProjectId,
                isIdeRequired = project.isIdeRequired
            )
        }

    private fun getTimeToComplete(secondsToComplete: Double): String {
        val hours = floor(secondsToComplete / 3600).toInt()

        return resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
    }
}