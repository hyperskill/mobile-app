package org.hyperskill.app.step_quiz_toolbar.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.subscriptions.domain.model.Subscription

object StepQuizToolbarFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(val subscription: Subscription) : State
    }

    internal fun initialState() = State.Idle

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        sealed interface Content : ViewState {
            object Hidden : Content
            data class Visible(val stepsLimitLabel: String) : Content
        }
    }

    sealed interface Message {
        object Initialize : Message

        object ProblemsLimitClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class SubscriptionFetchSuccess(val subscription: Subscription) : InternalMessage
        object SubscriptionFetchError : InternalMessage

        data class SubscriptionChanged(val subscription: Subscription) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction

        object FetchSubscription : InternalAction
    }
}