package org.hyperskill.app.step_feedback.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

object StepFeedbackFeature {
    internal data class State(
        val feedback: String?
    )

    internal fun initialState() =
        State(feedback = null)

    data class ViewState(
        val feedback: String,
        val isSendButtonEnabled: Boolean
    )

    sealed interface Message {
        object AlertShown : Message
        object AlertHidden : Message

        data class FeedbackTextChanged(val text: String?) : Message

        object SendButtonClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        object FeedbackSent : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object ShowSendSuccessAndHideModal : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
        data class SendFeedback(
            val route: HyperskillAnalyticRoute,
            val stepId: Long,
            val feedback: String
        ) : InternalAction
    }
}