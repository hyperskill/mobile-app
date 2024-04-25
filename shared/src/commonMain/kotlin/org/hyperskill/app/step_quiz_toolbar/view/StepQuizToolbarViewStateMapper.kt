package org.hyperskill.app.step_quiz_toolbar.view

import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.State
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.ViewState
import org.hyperskill.app.subscriptions.domain.model.areProblemsLimited

object StepQuizToolbarViewStateMapper {

    fun map(state: State): ViewState =
        when (state) {
            State.Idle -> ViewState.Idle
            State.Unavailable -> ViewState.Content.Hidden
            State.Loading -> ViewState.Loading
            State.Error -> ViewState.Error
            is State.Content -> {
                val stepsLimitLeft = state.subscription.stepsLimitLeft
                if (!state.subscription.areProblemsLimited || stepsLimitLeft == null) {
                    ViewState.Content.Hidden
                } else {
                    ViewState.Content.Visible(stepsLimitLabel = stepsLimitLeft.toString())
                }
            }
        }
}