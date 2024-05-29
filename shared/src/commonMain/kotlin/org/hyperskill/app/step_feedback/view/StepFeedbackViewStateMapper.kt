package org.hyperskill.app.step_feedback.view

import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.State
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.ViewState

internal object StepFeedbackViewStateMapper {
    fun map(state: State): ViewState =
        ViewState(
            feedback = state.feedback ?: "",
            isSendButtonEnabled = state.feedback?.isNotEmpty() == true
        )
}