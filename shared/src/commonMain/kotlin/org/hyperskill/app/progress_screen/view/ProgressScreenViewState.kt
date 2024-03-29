package org.hyperskill.app.progress_screen.view

import org.hyperskill.app.projects.domain.model.ProjectLevel

data class ProgressScreenViewState(
    val trackProgressViewState: TrackProgressViewState,
    val projectProgressViewState: ProjectProgressViewState,
    val isRefreshing: Boolean
) {
    val isInErrorState: Boolean
        get() = trackProgressViewState is TrackProgressViewState.Error &&
            projectProgressViewState is ProjectProgressViewState.Error

    sealed interface TrackProgressViewState {
        object Idle : TrackProgressViewState
        object Loading : TrackProgressViewState
        object Error : TrackProgressViewState
        data class Content(
            val title: String,

            val imageSource: String?,

            val completedTopicsCountLabel: String,
            val completedTopicsPercentageLabel: String,
            val completedTopicsPercentageProgress: Float,

            val appliedTopicsState: AppliedTopicsState,

            val timeToCompleteLabel: String?,

            val completedGraduateProjectsCount: Int?,

            val isCompleted: Boolean
        ) : TrackProgressViewState {
            sealed interface AppliedTopicsState {
                data class Content(
                    val countLabel: String,
                    val percentageLabel: String,
                    val percentageProgress: Float
                ) : AppliedTopicsState

                object Empty : AppliedTopicsState
            }
        }
    }

    sealed interface ProjectProgressViewState {
        object Idle : ProjectProgressViewState
        object Loading : ProjectProgressViewState
        object Error : ProjectProgressViewState
        object Empty : ProjectProgressViewState
        data class Content(
            val title: String,

            val level: ProjectLevel?,

            val timeToCompleteLabel: String?,

            val completedStagesLabel: String,
            val completedStagesProgress: Float,

            val isCompleted: Boolean
        ) : ProjectProgressViewState
    }
}