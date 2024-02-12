package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.subscriptions.domain.model.Subscription

object ManageSubscriptionFeature {
    internal sealed interface State {
        object Idle : State

        object Loading : State

        object Error : State

        data class Content(
            val subscription: Subscription,
            val manageSubscriptionUrl: String?
        ) : State
    }

    sealed interface ViewState {
        object Idle : ViewState

        object Loading : ViewState

        object Error : ViewState

        data class Content(
            val validUntilFormatted: String?,
            val isManageButtonVisible: Boolean
        ) : ViewState
    }

    sealed interface Message {
        object Initialize : Message
        object ViewedEventMessage : Message

        object RetryContentLoading : Message

        object ManageSubscriptionClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class FetchSubscriptionSuccess(
            val subscription: Subscription,
            val manageSubscriptionUrl: String?
        ) : InternalMessage

        object FetchSubscriptionError : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class OpenUrl(val url: String) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction

        object FetchSubscription : InternalAction
    }
}