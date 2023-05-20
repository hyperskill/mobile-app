package org.hyperskill.app.project_selection.view.mapper

import kotlin.math.floor
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
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
    private val numbersFormatter: NumbersFormatter
) {
    fun map(state: ProjectSelectionListFeature.ContentState): ProjectSelectionListFeature.ViewState =
        when (state) {
            ProjectSelectionListFeature.ContentState.Idle ->
                ProjectSelectionListFeature.ViewState.Idle
            ProjectSelectionListFeature.ContentState.Loading ->
                ProjectSelectionListFeature.ViewState.Loading
            is ProjectSelectionListFeature.ContentState.Content ->
                mapContentState(state)
            ProjectSelectionListFeature.ContentState.Error ->
                ProjectSelectionListFeature.ViewState.Error
        }

    private fun mapContentState(
        state: ProjectSelectionListFeature.ContentState.Content
    ): ProjectSelectionListFeature.ViewState.Content {
        val bestRatedProjectId = state.bestRatedProjectId
        val fastestToCompleteProjectId = state.fastestToCompleteProjectId
        return ProjectSelectionListFeature.ViewState.Content(
            trackIcon = state.track.cover.takeIf { it?.isNotBlank() ?: false },
            formattedTitle = resourceProvider.getString(
                SharedResources.strings.projects_list_title,
                state.track.title
            ),
            selectedProject = state.selectedProject?.let {
                mapProjectListItem(
                    state = state,
                    projectWithProgress = it,
                    bestRatedProjectId = bestRatedProjectId,
                    fastestToCompleteProjectId = fastestToCompleteProjectId
                )
            },
            recommendedProjects = state.recommendedProjects.map {
                mapProjectListItem(
                    state = state,
                    projectWithProgress = it,
                    bestRatedProjectId = bestRatedProjectId,
                    fastestToCompleteProjectId = fastestToCompleteProjectId
                )
            },
            projectsByLevel = state.projectsByLevel.mapValues { (level, projects) ->
                projects.map { project ->
                    mapProjectListItem(
                        state = state,
                        projectWithProgress = project,
                        bestRatedProjectId = bestRatedProjectId,
                        fastestToCompleteProjectId = fastestToCompleteProjectId,
                        level = level
                    )
                }
            },
            isProjectSelectionLoadingShowed = state.isProjectSelectionLoadingShowed
        )
    }

    private fun mapProjectListItem(
        state: ProjectSelectionListFeature.ContentState.Content,
        projectWithProgress: ProjectWithProgress,
        bestRatedProjectId: Long?,
        fastestToCompleteProjectId: Long?,
        level: ProjectLevel? = state.track.projectsByLevel.getProjectLevel(projectWithProgress.project.id)
    ): ProjectSelectionListFeature.ProjectListItem =
        with(projectWithProgress) {
            ProjectSelectionListFeature.ProjectListItem(
                id = project.id,
                title = project.title,
                averageRating = numbersFormatter.formatProgressAverageRating(progress.averageRating()),
                level = level,
                formattedTimeToComplete = getTimeToComplete(progress.secondsToComplete),
                isGraduate = project.isGraduate(state.track.id),
                isBestRated = project.id == bestRatedProjectId,
                isIdeRequired = project.isIdeRequired,
                isFastestToComplete = project.id == fastestToCompleteProjectId,
                isCompleted = progress.isCompleted
            )
        }

    private fun getTimeToComplete(secondsToComplete: Float?): String? {
        if (secondsToComplete == null || secondsToComplete <= 0) return null
        val hours = floor(secondsToComplete / 3600).toInt()
        return if (hours >= 1) {
            resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
        } else {
            null
        }
    }
}