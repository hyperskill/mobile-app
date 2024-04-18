package org.hyperskill.app.step_toolbar.view.mapper

import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature

internal object StepToolbarViewStateMapper {
    fun map(state: StepToolbarFeature.State): StepToolbarFeature.ViewState =
        when (state) {
            StepToolbarFeature.State.Idle -> StepToolbarFeature.ViewState.Idle
            StepToolbarFeature.State.Error -> StepToolbarFeature.ViewState.Error
            StepToolbarFeature.State.Loading -> StepToolbarFeature.ViewState.Loading
            is StepToolbarFeature.State.Content ->
                StepToolbarFeature.ViewState.Content(
                    progress = state.topicProgress.capacity ?: 0f
                )
        }
}