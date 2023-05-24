package org.hyperskill.app.project_selection_details.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.progresses.domain.model.averageRating
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.isGraduate
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.asLevelByProjectIdMap

internal class ProjectSelectionDetailsViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val numbersFormatter: NumbersFormatter,
    private val dateFormatter: SharedDateFormatter
) {
    fun map(state: ProjectSelectionDetailsFeature.State): ProjectSelectionDetailsFeature.ViewState =
        when (state.contentState) {
            ProjectSelectionDetailsFeature.ContentState.Idle ->
                ProjectSelectionDetailsFeature.ViewState.Idle
            ProjectSelectionDetailsFeature.ContentState.Loading ->
                ProjectSelectionDetailsFeature.ViewState.Loading
            ProjectSelectionDetailsFeature.ContentState.Error ->
                ProjectSelectionDetailsFeature.ViewState.Error
            is ProjectSelectionDetailsFeature.ContentState.Content ->
                mapContentState(state.contentState)
        }

    private fun mapContentState(
        state: ProjectSelectionDetailsFeature.ContentState.Content
    ): ProjectSelectionDetailsFeature.ViewState.Content {
        val track = state.data.track
        val projectWithProgress = state.data.project

        return ProjectSelectionDetailsFeature.ViewState.Content(
            formattedTitle = projectWithProgress.project.title.ifBlank {
                resourceProvider.getString(SharedResources.strings.projects_list_toolbar_title)
            },
            isIdeRequired = projectWithProgress.project.isIdeRequired,
            learningOutcomesDescription = projectWithProgress.project.results.ifBlank { null },
            formattedAverageRating = formatAverageRating(projectWithProgress.progress.averageRating()),
            formattedLevel = formatProjectLevel(track, projectWithProgress.project),
            formattedGraduateDescription = formatGraduateDescription(track.id, projectWithProgress.project),
            formattedTimeToComplete = dateFormatter.formatHoursCount(projectWithProgress.progress.secondsToComplete),
            providerName = state.data.provider?.title?.ifBlank { null }
        )
    }

    private fun formatAverageRating(averageRating: Double): String =
        if (averageRating > 0) {
            numbersFormatter.formatProgressAverageRating(averageRating)
        } else {
            resourceProvider.getString(SharedResources.strings.project_selection_details_no_rating)
        }

    private fun formatProjectLevel(track: Track, project: Project): String? {
        val levelByIdMap = track.projectsByLevel.asLevelByProjectIdMap()
        val projectLevel = levelByIdMap[project.id] ?: return null

        val levelName = when (projectLevel) {
            ProjectLevel.EASY ->
                resourceProvider.getString(SharedResources.strings.projects_list_easy_category_title)
            ProjectLevel.MEDIUM ->
                resourceProvider.getString(SharedResources.strings.projects_list_medium_category_title)
            ProjectLevel.HARD ->
                resourceProvider.getString(SharedResources.strings.projects_list_hard_category_title)
            ProjectLevel.NIGHTMARE ->
                resourceProvider.getString(SharedResources.strings.projects_list_nightmare_category_title)
        }

        return resourceProvider.getString(
            SharedResources.strings.project_selection_details_project_level,
            levelName
        )
    }

    private fun formatGraduateDescription(trackId: Long, project: Project): String? =
        if (project.isGraduate(trackId)) {
            resourceProvider.getString(SharedResources.strings.project_selection_details_graduate_project_description)
        } else {
            null
        }
}