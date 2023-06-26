package org.hyperskill.app.progresses.view

import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature
import org.hyperskill.app.track.domain.model.asLevelByProjectIdMap

internal class ProgressScreenViewStateMapper(
    private val dateFormatter: SharedDateFormatter
) {
    fun map(state: ProgressScreenFeature.State): ProgressScreenFeature.ViewState =
        when (state) {
            ProgressScreenFeature.State.Idle ->
                ProgressScreenFeature.ViewState.Idle
            ProgressScreenFeature.State.Loading ->
                ProgressScreenFeature.ViewState.Loading
            ProgressScreenFeature.State.Error ->
                ProgressScreenFeature.ViewState.Error
            is ProgressScreenFeature.State.Content ->
                mapContentState(state)
        }

    private fun mapContentState(
        contentState: ProgressScreenFeature.State.Content
    ): ProgressScreenFeature.ViewState.Content {

        val track = contentState.trackWithProgress.track
        val trackProgress = contentState.trackWithProgress.trackProgress

        val project = contentState.projectWithProgress.project
        val projectProgress = contentState.projectWithProgress.progress

        return ProgressScreenFeature.ViewState.Content(
            trackProgress = ProgressScreenFeature.ViewState.TrackProgressViewState(
                title = track.title,
                imageSource = track.cover?.takeIf { it.isNotBlank() },
                completedTopicsCountLabel = "${trackProgress.completedTopics} / ${track.topicsCount}",
                completedTopicsPercentageLabel = "${contentState.trackWithProgress.completedTopicsProgress}%",
                completedTopicsPercentageProgress = contentState.trackWithProgress.completedTopicsProgress / 100f,
                appliedTopicsCountLabel = "${trackProgress.appliedCapstoneTopicsCount} / ${track.capstoneTopicsCount}",
                appliedTopicsPercentageLabel = "${contentState.trackWithProgress.appliedTopicsProgress}%",
                appliedTopicsPercentageProgress = contentState.trackWithProgress.appliedTopicsProgress / 100f,
                timeToCompleteLabel = formatTimeToComplete(track.secondsToComplete),
                completedGraduateProjectsCount = trackProgress.completedCapstoneProjects.size,
                isCompleted = trackProgress.isCompleted
            ),
            projectProgress = ProgressScreenFeature.ViewState.ProjectProgressViewState(
                title = project.title,
                level = track.projectsByLevel.asLevelByProjectIdMap().get(project.id),
                timeToCompleteLabel = formatTimeToComplete(projectProgress.secondsToComplete),
                completedStagesLabel = "${projectProgress.completedStages?.size ?: 0} / ${project.stagesIds.size}",
                completedStagesProgress = contentState.projectWithProgress.progressPercentage / 100f,
                isCompleted = projectProgress.isCompleted
            )
        )
    }

    private fun formatTimeToComplete(secondsToComplete: Float?): String? =
        dateFormatter.formatHoursCount(secondsToComplete)?.let { formattedTime ->
            "~ $formattedTime"
        }
}