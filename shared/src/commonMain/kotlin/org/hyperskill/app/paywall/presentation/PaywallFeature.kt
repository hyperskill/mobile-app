package org.hyperskill.app.paywall.presentation

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType

object PaywallFeature {

    internal sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val formattedPrice: String,
            val isPurchaseSyncLoadingShowed: Boolean = false
        ) : State
    }

    internal fun initialState(): State =
        State.Idle

    data class ViewState(
        val isToolbarVisible: Boolean,
        val contentState: ViewStateContent
    )

    sealed interface ViewStateContent {
        object Idle : ViewStateContent
        object Loading : ViewStateContent
        object Error : ViewStateContent
        data class Content(
            val buyButtonText: String,
            val isContinueWithLimitsButtonVisible: Boolean
        ) : ViewStateContent

        object SubscriptionSyncLoading : ViewStateContent
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

            object StudyPlan : ViewAction

            data class ShowMessage(
                val messageKind: MessageKind
            ) : ViewAction
        }
    }

    enum class MessageKind(
        val stringRes: StringResource
    ) {
        GENERAL(SharedResources.strings.paywall_purchase_error_message),
        PENDING_PURCHASE(SharedResources.strings.paywall_pending_purchase),
        SUBSCRIPTION_WILL_BECOME_AVAILABLE_SOON(SharedResources.strings.paywall_subscription_sync_delayed)
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction

        object FetchMobileOnlyPrice : InternalAction

        data class StartMobileOnlySubscriptionPurchase(
            val purchaseParams: PlatformPurchaseParams
        ) : InternalAction

        object SyncSubscription : InternalAction

        data class LogWrongSubscriptionTypeAfterSync(
            val expectedSubscriptionType: SubscriptionType,
            val actualSubscriptionType: SubscriptionType
        ) : InternalAction
    }
}