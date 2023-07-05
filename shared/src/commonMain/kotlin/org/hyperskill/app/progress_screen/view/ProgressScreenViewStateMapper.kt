package org.hyperskill.app.progress_screen.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.asLevelByProjectIdMap

internal class ProgressScreenViewStateMapper(
    private val dateFormatter: SharedDateFormatter,
    private val resourceProvider: ResourceProvider
) {
    fun map(state: ProgressScreenFeature.State): ProgressScreenViewState =
        ProgressScreenViewState(
            trackProgressViewState = when (state.trackProgressState) {
                ProgressScreenFeature.TrackProgressState.Idle ->
                    ProgressScreenViewState.TrackProgressViewState.Idle
                ProgressScreenFeature.TrackProgressState.Loading ->
                    ProgressScreenViewState.TrackProgressViewState.Loading
                ProgressScreenFeature.TrackProgressState.Error ->
                    ProgressScreenViewState.TrackProgressViewState.Error
                is ProgressScreenFeature.TrackProgressState.Content ->
                    mapTrackProgressContent(state.trackProgressState)
            },
            projectProgressViewState = when (state.projectProgressState) {
                ProgressScreenFeature.ProjectProgressState.Idle ->
                    ProgressScreenViewState.ProjectProgressViewState.Idle
                ProgressScreenFeature.ProjectProgressState.Loading ->
                    ProgressScreenViewState.ProjectProgressViewState.Loading
                ProgressScreenFeature.ProjectProgressState.Error ->
                    ProgressScreenViewState.ProjectProgressViewState.Error
                ProgressScreenFeature.ProjectProgressState.Empty ->
                    ProgressScreenViewState.ProjectProgressViewState.Empty
                is ProgressScreenFeature.ProjectProgressState.Content ->
                    mapProjectProgressContent(
                        track = (state.trackProgressState as? ProgressScreenFeature.TrackProgressState.Content)
                            ?.trackWithProgress
                            ?.track,
                        projectProgressContent = state.projectProgressState
                    )
            },
            isRefreshing = state.isTrackProgressRefreshing || state.isProjectProgressRefreshing
        )

    private fun mapTrackProgressContent(
        trackProgressContent: ProgressScreenFeature.TrackProgressState.Content
    ): ProgressScreenViewState.TrackProgressViewState.Content {
        val track = trackProgressContent.trackWithProgress.track
        val trackProgress = trackProgressContent.trackWithProgress.trackProgress

        return ProgressScreenViewState.TrackProgressViewState.Content(
            title = track.title,
            imageSource = track.cover?.takeIf { it.isNotBlank() },
            completedTopicsCountLabel = "${trackProgress.completedTopics} / ${track.topicsCount}",
            completedTopicsPercentageLabel = "• ${trackProgressContent.trackWithProgress.completedTopicsProgress}%",
            completedTopicsPercentageProgress = trackProgressContent.trackWithProgress.completedTopicsProgress / 100f,
            appliedTopicsCountLabel = "${trackProgress.appliedCapstoneTopicsCount} / ${track.capstoneTopicsCount}",
            appliedTopicsPercentageLabel = "• ${trackProgressContent.trackWithProgress.appliedTopicsProgress}%",
            appliedTopicsPercentageProgress = trackProgressContent.trackWithProgress.appliedTopicsProgress / 100f,
            timeToCompleteLabel = if (trackProgress.isCompleted) {
                resourceProvider.getString(SharedResources.strings.completed)
            } else if (trackProgressContent.profile.gamification.passedTopicsCount < 3)
                formatTimeToComplete(track.secondsToComplete)
            else {
                formatTimeToComplete(trackProgressContent.studyPlan.secondsToReachTrack)
            },
            completedGraduateProjectsCount = trackProgress.completedCapstoneProjects.size,
            isCompleted = trackProgress.isCompleted
        )
    }

    private fun mapProjectProgressContent(
        track: Track?,
        projectProgressContent: ProgressScreenFeature.ProjectProgressState.Content
    ): ProgressScreenViewState.ProjectProgressViewState.Content {
        val project = projectProgressContent.projectWithProgress.project
        val projectProgress = projectProgressContent.projectWithProgress.progress

        return ProgressScreenViewState.ProjectProgressViewState.Content(
            title = project.title,
            level = track?.projectsByLevel?.asLevelByProjectIdMap()?.get(project.id),
            timeToCompleteLabel = if (projectProgress.isCompleted) {
                resourceProvider.getString(SharedResources.strings.completed)
            } else {
                formatTimeToComplete(projectProgressContent.studyPlan.secondsToReachProject)
            },
            completedStagesLabel = "${projectProgress.completedStages.size} / ${project.stagesIds.size}",
            completedStagesProgress = projectProgressContent.projectWithProgress.progressPercentage / 100f,
            isCompleted = projectProgress.isCompleted
        )
    }

    private fun formatTimeToComplete(secondsToComplete: Float?): String? =
        dateFormatter.formatHoursCount(secondsToComplete)?.let { formattedTime ->
            "~ $formattedTime"
        }
}