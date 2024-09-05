package org.hyperskill.app.paywall.presentation

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.purchases.domain.model.PlatformPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import org.hyperskill.app.purchases.domain.model.SubscriptionOption
import org.hyperskill.app.purchases.domain.model.SubscriptionProduct
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType

object PaywallFeature {
    internal sealed interface State {
        data object Idle : State
        data object Loading : State
        data object Error : State
        data class Content(
            val subscriptionProducts: List<SubscriptionProduct>,
            val selectedProductId: String,
            val isPurchaseSyncLoadingShowed: Boolean = false
        ) : State
    }

    data class ViewState(
        val isToolbarVisible: Boolean,
        val contentState: ViewStateContent
    )

    sealed interface ViewStateContent {
        data object Idle : ViewStateContent
        data object Loading : ViewStateContent
        data object Error : ViewStateContent
        data class Content(
            val subscriptionProducts: List<SubscriptionProduct>,
            val buyButtonText: String,
            val trialText: String? = null
        ) : ViewStateContent

        data object SubscriptionSyncLoading : ViewStateContent

        data class SubscriptionProduct(
            val productId: String,
            val title: String,
            val subtitle: String,
            val isBestValue: Boolean,
            val isSelected: Boolean
        )
    }

    sealed interface Message {
        data object Initialize : Message

        data object RetryContentLoading : Message

        data object CloseClicked : Message

        data class ProductClicked(val productId: String) : Message

        data class BuySubscriptionClicked(
            val purchaseParams: PlatformPurchaseParams
        ) : Message

        data object ClickedTermsOfServiceAndPrivacyPolicy : Message

        data object ScreenShowed : Message
        data object ScreenHidden : Message

        data object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        data object FetchSubscriptionProductsError : InternalMessage
        data class FetchSubscriptionProductsSuccess(
            val subscriptionProducts: List<SubscriptionProduct>
        ) : InternalMessage

        data object MobileOnlySubscriptionPurchaseError : InternalMessage
        data class MobileOnlySubscriptionPurchaseSuccess(
            val purchaseResult: PurchaseResult
        ) : InternalMessage

        data object SubscriptionSyncError : InternalMessage
        data class SubscriptionSyncSuccess(val subscription: Subscription) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data object ClosePaywall : ViewAction

            data class ShowMessage(
                val messageKind: MessageKind
            ) : ViewAction

            data class OpenUrl(val url: String) : ViewAction

            data class NotifyPaywallIsShown(val isPaywallShown: Boolean) : ViewAction

            sealed interface NavigateTo : ViewAction {
                data object Back : NavigateTo
                data object BackToProfileSettings : NavigateTo
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
        data object FetchMobileOnlyPrice : InternalAction

        data class StartSubscriptionProductPurchase(
            val subscriptionOption: SubscriptionOption,
            val purchaseParams: PlatformPurchaseParams
        ) : InternalAction

        data object SyncSubscription : InternalAction

        data class LogWrongSubscriptionTypeAfterSync(
            val expectedSubscriptionType: SubscriptionType,
            val actualSubscriptionType: SubscriptionType
        ) : InternalAction

        class LogAnalyticEvent(vararg val analyticEvents: AnalyticEvent) : InternalAction
    }
}