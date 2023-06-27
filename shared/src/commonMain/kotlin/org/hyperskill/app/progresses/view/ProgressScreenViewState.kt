package org.hyperskill.app.progresses.view

import org.hyperskill.app.projects.domain.model.ProjectLevel

data class ProgressScreenViewState(
    val trackProgressViewState: TrackProgressViewState,
    val projectProgressViewState: ProjectProgressViewState
) {
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

            val appliedTopicsCountLabel: String,
            val appliedTopicsPercentageLabel: String,
            val appliedTopicsPercentageProgress: Float,

            val timeToCompleteLabel: String?,

            val completedGraduateProjectsCount: Int,

            val isCompleted: Boolean
        ) : TrackProgressViewState
    }

    sealed interface ProjectProgressViewState {
        object Idle : ProjectProgressViewState
        object Loading : ProjectProgressViewState
        object Error : ProjectProgressViewState
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