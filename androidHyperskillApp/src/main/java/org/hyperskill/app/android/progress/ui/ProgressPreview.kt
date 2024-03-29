package org.hyperskill.app.android.progress.ui

import org.hyperskill.app.progress_screen.view.ProgressScreenViewState
import org.hyperskill.app.projects.domain.model.ProjectLevel

object ProgressPreview {
    fun trackContentViewStatePreview(
        isCompleted: Boolean = false
    ): ProgressScreenViewState.TrackProgressViewState.Content =
        ProgressScreenViewState.TrackProgressViewState.Content(
            title = "Kotlin for Beginners",
            imageSource = null,
            completedTopicsCountLabel = "0 / 147",
            completedTopicsPercentageLabel = "34%",
            completedTopicsPercentageProgress = 0.34f,
            appliedTopicsState = ProgressScreenViewState.TrackProgressViewState.Content.AppliedTopicsState.Content(
                countLabel = "34 / 150",
                percentageLabel = "67",
                percentageProgress = 0.67f
            ),
            timeToCompleteLabel = "~ 56h",
            completedGraduateProjectsCount = 50,
            isCompleted = isCompleted
        )

    fun trackErrorPreview(): ProgressScreenViewState.TrackProgressViewState.Error =
        ProgressScreenViewState.TrackProgressViewState.Error

    fun projectContentViewStatePreview(
        isCompleted: Boolean = false
    ): ProgressScreenViewState.ProjectProgressViewState.Content =
        ProgressScreenViewState.ProjectProgressViewState.Content(
            title = "Simple Chatty Bot",
            level = ProjectLevel.EASY,
            timeToCompleteLabel = "~ 56h",
            completedStagesLabel = "0 / 5",
            completedStagesProgress = 0.2f,
            isCompleted = isCompleted
        )

    fun projectErrorPreview(): ProgressScreenViewState.ProjectProgressViewState.Error =
        ProgressScreenViewState.ProjectProgressViewState.Error
}