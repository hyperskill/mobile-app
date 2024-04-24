package org.hyperskill.app.step.view.mapper

import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step_toolbar.view.mapper.StepToolbarViewStateMapper

internal object StepViewStateMapper {
    fun map(state: StepFeature.State): StepFeature.ViewState =
        StepFeature.ViewState(
            stepState = state.stepState,
            stepToolbarViewState = StepToolbarViewStateMapper.map(state.stepToolbarState)
        )
}