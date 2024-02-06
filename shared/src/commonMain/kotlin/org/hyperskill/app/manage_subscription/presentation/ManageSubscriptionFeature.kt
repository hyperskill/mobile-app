package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object ManageSubscriptionFeature {
    sealed interface State {
        object Idle : State

        object Loading : State

        object Error : State

        data class Content(
            val manageSubscriptionUrl: String
        )
    }

    sealed interface Message {
        object Initialize : Message
        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction
    }
}