package org.hyperskill.app.step.view.mapper

import org.hyperskill.app.comments.domain.model.CommentThread
import org.hyperskill.app.step.domain.model.StepMenuAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step_toolbar.view.mapper.StepToolbarViewStateMapper

internal class StepViewStateMapper(
    private val stepRoute: StepRoute
) {
    fun map(state: StepFeature.State): StepFeature.ViewState =
        StepFeature.ViewState(
            stepState = state.stepState,
            stepToolbarViewState = StepToolbarViewStateMapper.map(state.stepToolbarState),
            stepMenuActions = getStepToolbarActions(state.stepState, stepRoute),
            isCommentsToolbarItemAvailable = isCommentsToolbarItemAvailable(state.stepState),
            isLoadingShowed = state.isLoadingShowed
        )

    private fun getStepToolbarActions(
        stepState: StepFeature.StepState,
        stepRoute: StepRoute
    ): Set<StepMenuAction> =
        StepMenuAction.values().filter { action ->
            action != StepMenuAction.SKIP || isSkipButtonAvailable(stepState, stepRoute)
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
        return step.commentsStatistics
            .firstOrNull { it.thread == CommentThread.COMMENT }
            ?.totalCount
            ?.let { it > 0 } ?: false
    }
}