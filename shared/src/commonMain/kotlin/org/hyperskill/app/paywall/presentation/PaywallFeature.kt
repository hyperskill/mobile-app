package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import org.hyperskill.app.subscriptions.domain.model.Subscription

object PaywallFeature {

    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val formattedPrice: String,
            val isPurchaseSyncLoadingShowed: Boolean = false
        ) : State
    }

    fun initialState(): State =
        State.Idle

    data class ViewState(
        val isToolbarVisible: Boolean,
        val contentState: ViewContentState
    )

    sealed interface ViewContentState {
        object Idle : ViewContentState
        object Loading : ViewContentState
        object Error : ViewContentState
        data class Content(
            val buyButtonText: String,
            val isContinueWithLimitsButtonVisible: Boolean
        ) : ViewContentState
    }

    sealed interface Message {
        object Initialize : Message

        object RetryContentLoading : Message

        object ViewedEventMessage : Message

        object ContinueWithLimitsClicked : Message

        data class BuySubscriptionClicked(
            val purchaseParams: PlatformPurchaseParams
        ) : Message
    }

    internal sealed interface InternalMessage : Message {
        data class FetchMobileOnlyPriceSuccess(val formattedPrice: String) : InternalMessage

        object FetchMobileOnlyPriceError : InternalMessage

        data class MobileOnlySubscriptionPurchaseSuccess(
            val purchaseResult: PurchaseResult
        ) : InternalMessage

        object MobileOnlySubscriptionPurchaseError : InternalMessage

        data class SubscriptionSyncSuccess(val subscription: Subscription) : InternalMessage

        object SubscriptionSyncError : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object CompletePaywall : ViewAction

            object ShowPurchaseError : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction

        object FetchMobileOnlyPrice : InternalAction

        data class StartMobileOnlySubscriptionPurchase(
            val purchaseParams: PlatformPurchaseParams
        ) : InternalAction

        object SyncSubscription : InternalAction
    }
}