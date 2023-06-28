package org.hyperskill.app.progresses.view

import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.asLevelByProjectIdMap

internal class ProgressScreenViewStateMapper(
    private val dateFormatter: SharedDateFormatter
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
                is ProgressScreenFeature.ProjectProgressState.Content ->
                    when (state.trackProgressState) {
                        ProgressScreenFeature.TrackProgressState.Idle,
                        ProgressScreenFeature.TrackProgressState.Loading ->
                            ProgressScreenViewState.ProjectProgressViewState.Loading
                        ProgressScreenFeature.TrackProgressState.Error ->
                            ProgressScreenViewState.ProjectProgressViewState.Error
                        is ProgressScreenFeature.TrackProgressState.Content ->
                            mapProjectProgressContent(
                                track = state.trackProgressState.trackWithProgress.track,
                                projectProgressContent = state.projectProgressState
                            )
                    }
            },
            isRefreshing = state.isTrackProgressRefreshing && state.isProjectProgressRefreshing
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
            completedTopicsPercentageLabel = "${trackProgressContent.trackWithProgress.completedTopicsProgress}%",
            completedTopicsPercentageProgress = trackProgressContent.trackWithProgress.completedTopicsProgress / 100f,
            appliedTopicsCountLabel = "${trackProgress.appliedCapstoneTopicsCount} / ${track.capstoneTopicsCount}",
            appliedTopicsPercentageLabel = "${trackProgressContent.trackWithProgress.appliedTopicsProgress}%",
            appliedTopicsPercentageProgress = trackProgressContent.trackWithProgress.appliedTopicsProgress / 100f,
            timeToCompleteLabel = formatTimeToComplete(track.secondsToComplete),
            completedGraduateProjectsCount = trackProgress.completedCapstoneProjects.size,
            isCompleted = trackProgress.isCompleted
        )
    }

    private fun mapProjectProgressContent(
        track: Track,
        projectProgressContent: ProgressScreenFeature.ProjectProgressState.Content
    ): ProgressScreenViewState.ProjectProgressViewState.Content {
        val project = projectProgressContent.projectWithProgress.project
        val projectProgress = projectProgressContent.projectWithProgress.progress

        return ProgressScreenViewState.ProjectProgressViewState.Content(
            title = project.title,
            level = track.projectsByLevel.asLevelByProjectIdMap().get(project.id),
            timeToCompleteLabel = formatTimeToComplete(projectProgress.secondsToComplete),
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