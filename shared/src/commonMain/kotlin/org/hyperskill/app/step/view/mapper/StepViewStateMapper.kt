package org.hyperskill.app.step.view.mapper

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.domain.model.StepToolbarAction
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step_toolbar.view.mapper.StepToolbarViewStateMapper

internal class StepViewStateMapper(
    private val stepRoute: StepRoute
) {
    fun map(state: StepFeature.State): StepFeature.ViewState =
        StepFeature.ViewState(
            stepState = state.stepState,
            stepToolbarViewState = StepToolbarViewStateMapper.map(state.stepToolbarState),
            stepToolbarActions = getStepToolbarActions(state.stepState, stepRoute)
        )

    private fun getStepToolbarActions(
        stepState: StepFeature.StepState,
        stepRoute: StepRoute
    ): Set<StepToolbarAction> =
        StepToolbarAction.values().filter { action ->
            action != StepToolbarAction.SKIP || isSkipButtonAvailable(stepState, stepRoute)
        }.toSet()

    private fun isSkipButtonAvailable(
        stepState: StepFeature.StepState,
        stepRoute: StepRoute
    ): Boolean =
        stepState is StepFeature.StepState.Data &&
            stepState.step.canSkip &&
                stepRoute is StepRoute.Learn
}