package org.hyperskill.app.project_selection.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.progresses.domain.model.averageRating
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature
import org.hyperskill.app.project_selection.presentation.bestRatedProjectId
import org.hyperskill.app.project_selection.presentation.fastestToCompleteProjectId
import org.hyperskill.app.project_selection.presentation.projectsByLevel
import org.hyperskill.app.project_selection.presentation.recommendedProjects
import org.hyperskill.app.project_selection.presentation.selectedProject
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.isGraduate
import org.hyperskill.app.track.domain.model.getProjectLevel

internal class ProjectSelectionListViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val numbersFormatter: NumbersFormatter,
    private val dateFormatter: SharedDateFormatter
) {
    fun map(state: ProjectSelectionListFeature.ContentState): ProjectSelectionListFeature.ViewState =
        when (state) {
            ProjectSelectionListFeature.ContentState.Idle -> ProjectSelectionListFeature.ViewState.Idle
            ProjectSelectionListFeature.ContentState.Loading -> ProjectSelectionListFeature.ViewState.Loading
            is ProjectSelectionListFeature.ContentState.Content -> {
                val bestRatedProjectId = state.bestRatedProjectId
                val fastestToCompleteProjectId = state.fastestToCompleteProjectId
                ProjectSelectionListFeature.ViewState.Content(
                    trackIcon = state.track.cover.takeIf { it?.isNotBlank() ?: false },
                    formattedTitle = resourceProvider.getString(
                        SharedResources.strings.projects_list_title,
                        state.track.title
                    ),
                    selectedProject = state.selectedProject?.let {
                        mapProjectListItem(
                            projectWithProgress = it,
                            trackId = state.track.id,
                            bestRatedProjectId = bestRatedProjectId,
                            fastestToCompleteProjectId = fastestToCompleteProjectId,
                            level = state.track.projectsByLevel.getProjectLevel(it.project.id)
                        )
                    },
                    recommendedProjects = state.recommendedProjects.map {
                        mapProjectListItem(
                            projectWithProgress = it,
                            trackId = state.track.id,
                            bestRatedProjectId = bestRatedProjectId,
                            fastestToCompleteProjectId = fastestToCompleteProjectId,
                            level = state.track.projectsByLevel.getProjectLevel(it.project.id)
                        )
                    },
                    projectsByLevel = state.projectsByLevel.mapValues { (level, projects) ->
                        projects.map { project ->
                            mapProjectListItem(
                                projectWithProgress = project,
                                trackId = state.track.id,
                                bestRatedProjectId = bestRatedProjectId,
                                fastestToCompleteProjectId = fastestToCompleteProjectId,
                                level = level
                            )
                        }
                    },
                    isProjectSelectionLoadingShowed = state.isProjectSelectionLoadingShowed
                )
            }
            ProjectSelectionListFeature.ContentState.Error -> ProjectSelectionListFeature.ViewState.Error
        }

    private fun mapProjectListItem(
        projectWithProgress: ProjectWithProgress,
        trackId: Long,
        bestRatedProjectId: Long?,
        fastestToCompleteProjectId: Long?,
        level: ProjectLevel?
    ): ProjectSelectionListFeature.ProjectListItem =
        with(projectWithProgress) {
            ProjectSelectionListFeature.ProjectListItem(
                id = project.id,
                title = project.title,
                averageRating = numbersFormatter.formatProgressAverageRating(progress.averageRating()),
                level = level,
                formattedTimeToComplete = dateFormatter.hoursCount(progress.secondsToComplete),
                isGraduate = project.isGraduate(trackId),
                isBestRated = project.id == bestRatedProjectId,
                isIdeRequired = project.isIdeRequired,
                isFastestToComplete = project.id == fastestToCompleteProjectId,
                isCompleted = progress.isCompleted
            )
        }
}