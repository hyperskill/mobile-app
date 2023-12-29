package org.hyperskill.app.interview_preparation.view.model

sealed interface InterviewPreparationWidgetViewState {
    object Idle : InterviewPreparationWidgetViewState
    data class Loading(val shouldShowSkeleton: Boolean) : InterviewPreparationWidgetViewState
    object Error : InterviewPreparationWidgetViewState
    object Empty : InterviewPreparationWidgetViewState

    data class Content(
        val stepsCount: Int,
        val description: String
    ) : InterviewPreparationWidgetViewState
}