package org.hyperskill.app.step.view.mapper

import org.hyperskill.app.step.domain.model.Step
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
            stepMenuSecondaryActions = getStepMenuSecondaryActions(state.stepState, stepRoute),
            isLoadingShowed = state.isLoadingShowed,
            isCommentsToolbarItemAvailable = isPrimaryCommentsToolbarItemAvailable(state.stepState)
        )

    private fun getStepMenuSecondaryActions(
        stepState: StepFeature.StepState,
        stepRoute: StepRoute
    ): Set<StepMenuSecondaryAction> =
        StepMenuSecondaryAction.entries.filter { action ->
            when (action) {
                StepMenuSecondaryAction.COMMENTS -> isSecondaryCommentsMenuActionAvailable(stepState)
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

    private fun isPrimaryCommentsToolbarItemAvailable(stepState: StepFeature.StepState): Boolean {
        val step = (stepState as? StepFeature.StepState.Data)?.step ?: return false
        return step.type == Step.Type.THEORY && step.areCommentsAvailable
    }

    private fun isSecondaryCommentsMenuActionAvailable(stepState: StepFeature.StepState): Boolean {
        val step = (stepState as? StepFeature.StepState.Data)?.step ?: return false
        return step.type == Step.Type.PRACTICE && step.areCommentsAvailable
    }
}