package org.hyperskill.app.theory_feedback.view

import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.State
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.ViewState

internal object TheoryFeedbackViewStateMapper {

    fun map(state: State): ViewState =
        ViewState(
            feedback = state.feedback ?: "",
            isSendButtonEnabled = state.feedback?.isNotEmpty() == true
        )
}