package org.hyperskill.app.step.view.mapper

import org.hyperskill.app.step.domain.model.StepMenuSecondaryAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.domain.model.areCommentsAvailable
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step_toolbar.view.mapper.StepToolbarViewStateMapper

internal class StepViewStateMapper(
    private val stepRoute: StepRoute
) {
    fun map(state: StepFeature.State): StepFeature.ViewState =
        StepFeature.ViewState(
            stepState = state.stepState,
            stepToolbarViewState = StepToolbarViewStateMapper.map(state.stepToolbarState),
            stepMenuSecondaryActions = getStepToolbarActions(state.stepState, stepRoute),
            isLoadingShowed = state.isLoadingShowed
        )

    private fun getStepToolbarActions(
        stepState: StepFeature.StepState,
        stepRoute: StepRoute
    ): Set<StepMenuSecondaryAction> =
        StepMenuSecondaryAction.entries.filter { action ->
            when (action) {
                StepMenuSecondaryAction.СOMMENTS -> isCommentsToolbarItemAvailable(stepState)
                StepMenuSecondaryAction.SKIP -> isSkipButtonAvailable(stepState, stepRoute)
                StepMenuSecondaryAction.SHARE,
                StepMenuSecondaryAction.REPORT,
                StepMenuSecondaryAction.OPEN_IN_WEB -> true
            }
        }.toSet()

    private fun isSkipButtonAvailable(
        stepState: StepFeature.StepState,
        stepRoute: StepRoute
    ): Boolean =
        stepState is StepFeature.StepState.Data &&
            stepRoute is StepRoute.Learn.Step &&
            stepState.step.canSkip

    private fun isCommentsToolbarItemAvailable(stepState: StepFeature.StepState): Boolean {
        val step = (stepState as? StepFeature.StepState.Data)?.step ?: return false
        return step.areCommentsAvailable
    }
}