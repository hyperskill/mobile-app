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
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.isGraduate
import org.hyperskill.app.track.domain.model.asLevelByProjectIdMap

internal class ProjectSelectionListViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val numbersFormatter: NumbersFormatter,
    private val dateFormatter: SharedDateFormatter
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
        val levelByProjectIdMap = state.track.projectsByLevel.asLevelByProjectIdMap()
        val betaProjectIds = state.track.betaProjects.toSet()

        fun mapProjectListItem(projectWithProgress: ProjectWithProgress): ProjectSelectionListFeature.ProjectListItem =
            with(projectWithProgress) {
                ProjectSelectionListFeature.ProjectListItem(
                    id = project.id,
                    title = project.title,
                    averageRating = numbersFormatter.formatProgressAverageRating(progress.averageRating()),
                    level = levelByProjectIdMap[projectWithProgress.project.id],
                    formattedTimeToComplete = dateFormatter.formatHoursCount(progress.secondsToComplete),
                    isGraduate = project.isGraduate(state.track.id),
                    isBestRated = projectWithProgress.project.id == bestRatedProjectId,
                    isIdeRequired = project.isIdeRequired,
                    isFastestToComplete = projectWithProgress.project.id == fastestToCompleteProjectId,
                    isCompleted = progress.isCompleted,
                    isBeta = betaProjectIds.contains(projectWithProgress.project.id)
                )
            }

        return ProjectSelectionListFeature.ViewState.Content(
            trackIcon = state.track.cover.takeIf { it?.isNotBlank() ?: false },
            formattedTitle = resourceProvider.getString(
                SharedResources.strings.projects_list_title,
                state.track.title
            ),
            selectedProject = state.selectedProject?.let(::mapProjectListItem),
            recommendedProjects = state.recommendedProjects.map(::mapProjectListItem),
            projectsByLevel = state.projectsByLevel(levelByProjectIdMap).mapValues { (_, projects) ->
                projects.map(::mapProjectListItem)
            }
        )
    }
}