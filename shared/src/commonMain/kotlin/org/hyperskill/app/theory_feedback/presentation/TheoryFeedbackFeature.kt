package org.hyperskill.app.theory_feedback.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object TheoryFeedbackFeature {
    internal data class State(
        val feedback: String
    )

    internal fun initialState() =
        State(feedback = "")

    data class ViewState(
        val feedback: String,
        val isSendButtonEnabled: Boolean
    )

    sealed interface Message {
        object Initialize : Message
        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
    }
}