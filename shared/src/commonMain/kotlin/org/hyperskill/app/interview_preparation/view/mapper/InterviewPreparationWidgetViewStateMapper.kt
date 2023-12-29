package org.hyperskill.app.interview_preparation.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.State
import org.hyperskill.app.interview_preparation.view.model.InterviewPreparationWidgetViewState

class InterviewPreparationWidgetViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: State): InterviewPreparationWidgetViewState =
        when (state) {
            State.Idle -> InterviewPreparationWidgetViewState.Idle
            State.Error -> InterviewPreparationWidgetViewState.Error
            is State.Loading ->
                InterviewPreparationWidgetViewState.Loading(shouldShowSkeleton = !state.isLoadingSilently)
            is State.Content -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: State.Content): InterviewPreparationWidgetViewState {
        val stepsCount = state.steps.count()
        return InterviewPreparationWidgetViewState.Content(
            stepsCount = stepsCount,
            description = resourceProvider.getQuantityString(
                SharedResources.plurals.interview_preparation_widget_description,
                stepsCount
            )
        )
    }
}