package org.hyperskill.app.progresses.view

import org.hyperskill.app.projects.domain.model.ProjectLevel

sealed interface ProgressScreenViewState {
    object Idle : ProgressScreenViewState
    object Loading : ProgressScreenViewState
    object Error : ProgressScreenViewState
    data class Content(
        val trackProgress: TrackProgressViewState,
        val projectProgress: ProjectProgressViewState?
    ) : ProgressScreenViewState

    data class TrackProgressViewState(
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
    )

    data class ProjectProgressViewState(
        val title: String,

        val level: ProjectLevel?,

        val timeToCompleteLabel: String?,

        val completedStagesLabel: String,
        val completedStagesProgress: Float,

        val isCompleted: Boolean
    )
}