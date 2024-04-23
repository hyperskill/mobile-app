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

        object ContinueWithLimitsClicked : Message

        data class BuySubscriptionClicked(
            val purchaseParams: PlatformPurchaseParams
        ) : Message

        object ClickedTermsOfServiceAndPrivacyPolicy : Message

        object ScreenShowed : Message
        object ScreenHidden : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        object FetchMobileOnlyPriceError : InternalMessage
        data class FetchMobileOnlyPriceSuccess(val formattedPrice: String) : InternalMessage

        object MobileOnlySubscriptionPurchaseError : InternalMessage
        data class MobileOnlySubscriptionPurchaseSuccess(
            val purchaseResult: PurchaseResult
        ) : InternalMessage

        object SubscriptionSyncError : InternalMessage
        data class SubscriptionSyncSuccess(val subscription: Subscription) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object ClosePaywall : ViewAction

            data class ShowMessage(
                val messageKind: MessageKind
            ) : ViewAction

            data class OpenUrl(val url: String) : ViewAction

            data class NotifyPaywallIsShown(val isPaywallShown: Boolean) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
                object BackToProfileSettings : NavigateTo
            }
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
        object FetchMobileOnlyPrice : InternalAction

        data class StartMobileOnlySubscriptionPurchase(
            val purchaseParams: PlatformPurchaseParams
        ) : InternalAction

        object SyncSubscription : InternalAction

        data class LogWrongSubscriptionTypeAfterSync(
            val expectedSubscriptionType: SubscriptionType,
            val actualSubscriptionType: SubscriptionType
        ) : InternalAction

        class LogAnalyticEvent(vararg val analyticEvent: AnalyticEvent) : InternalAction
    }
}