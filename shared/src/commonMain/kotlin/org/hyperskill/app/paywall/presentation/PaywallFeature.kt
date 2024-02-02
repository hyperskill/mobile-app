package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object PaywallFeature {

    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(val formattedPrice: String) : State
    }

    fun initialState(): State =
        State.Idle

    data class ViewState(
        val isToolbarVisible: Boolean,
        val contentState: ContentState
    ) {
        sealed interface ContentState {
            object Idle : ContentState
            object Loading : ContentState
            object Error : ContentState
            data class Content(
                val buyButtonText: String,
                val isContinueWithLimitsButtonVisible: Boolean
            ) : ContentState
        }
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