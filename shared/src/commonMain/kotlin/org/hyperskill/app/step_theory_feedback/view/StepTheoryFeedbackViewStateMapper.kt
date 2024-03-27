package org.hyperskill.app.step_theory_feedback.view

import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.State
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.ViewState

internal object StepTheoryFeedbackViewStateMapper {
    fun map(state: State): ViewState =
        ViewState(
            feedback = state.feedback ?: "",
            isSendButtonEnabled = state.feedback?.isNotEmpty() == true
        )
}