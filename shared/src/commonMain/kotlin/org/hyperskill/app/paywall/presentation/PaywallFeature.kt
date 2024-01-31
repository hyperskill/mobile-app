package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource

object PaywallFeature {

    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val formattedPrice: String,
            val paywallTransitionSource: PaywallTransitionSource
        ) : State
    }

    fun initialState(): State =
        State.Idle

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val buyButtonText: String,
            val isToolbarVisible: Boolean,
            val isContinueWithLimitsButtonVisible: Boolean
        ) : ViewState
    }

    sealed interface Message {
        object Initialize : Message

        object RetryContentLoading : Message

        object ViewedEventMessage : Message

        object ContinueWithLimitsClicked : Message

        object BuySubscriptionClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class FetchMobileOnlyPriceSuccess(val formattedPrice: String) : InternalMessage

        object FetchMobileOnlyPriceError : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object CompletePaywall : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction

        object FetchMobileOnlyPrice : InternalAction
    }
}