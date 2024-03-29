package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
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
            val buttonText: String?
        ) : ViewState
    }

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message

        object ActionButtonClicked : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        object FetchSubscriptionError : InternalMessage
        data class FetchSubscriptionSuccess(
            val subscription: Subscription,
            val manageSubscriptionUrl: String?
        ) : InternalMessage

        data class SubscriptionChanged(
            val subscription: Subscription,
            val manageSubscriptionUrl: String?
        ) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class OpenUrl(val url: String) : ViewAction

            sealed interface NavigateTo : ViewAction {
                data class Paywall(val paywallTransitionSource: PaywallTransitionSource) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchSubscription : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}