package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.subscriptions.domain.model.Subscription

object ManageSubscriptionFeature {
    sealed interface State {
        object Idle : State

        object Loading : State

        object Error : State

        data class Content(
            val subscription: Subscription,
            val manageSubscriptionUrl: String?
        ) : State
    }

    sealed interface Message {
        object Initialize : Message
        object ViewedEventMessage : Message

        object RetryContentLoading : Message
    }

    internal sealed interface InternalMessage : Message {
        data class FetchSubscriptionSuccess(
            val subscription: Subscription,
            val manageSubscriptionUrl: String?
        ) : InternalMessage

        object FetchSubscriptionError : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction

        data class FetchSubscription(val forceLoadFromNetwork: Boolean) : InternalAction
    }
}