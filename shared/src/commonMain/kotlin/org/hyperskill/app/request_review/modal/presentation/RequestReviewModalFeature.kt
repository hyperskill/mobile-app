package org.hyperskill.app.request_review.modal.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object RequestReviewModalFeature {
    internal sealed interface State {
        object Awaiting : State
        object Negative : State
        object Positive : State
    }

    data class ViewState(
        val title: String,
        val description: String?,
        val positiveButtonText: String,
        val negativeButtonText: String,
        val state: State
    ) {
        enum class State {
            AWAITING,
            NEGATIVE,
            POSITIVE
        }
    }

    sealed interface Message {
        object PositiveButtonClicked : Message
        object NegativeButtonClicked : Message

        /**
         * Analytic
         */
        object ShownEventMessage : Message
        object HiddenEventMessage : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action {
            object RequestUserReview : ViewAction

            data class SubmitSupportRequest(val url: String) : ViewAction

            object Dismiss : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}