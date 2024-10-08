package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedBuySubscriptionHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedCloseButtonHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedProductHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedTermsOfServiceAndPrivacyPolicyHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallSubscriptionPurchasedAmplitudeAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallSubscriptionPurchasedAppsFlyerAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallViewedHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalMessage
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class PaywallReducer(
    private val paywallTransitionSource: PaywallTransitionSource,
    private val resourceProvider: ResourceProvider
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.Initialize -> fetchMobileOnlyPrice()
            Message.RetryContentLoading ->
                fetchMobileOnlyPrice(
                    setOf(
                        InternalAction.LogAnalyticEvent(
                            PaywallClickedRetryContentLoadingHyperskillAnalyticEvent(paywallTransitionSource)
                        )
                    )
                )
            is InternalMessage.FetchSubscriptionProductsSuccess ->
                handleFetchMobileOnlyPriceSuccess(message)
            InternalMessage.FetchSubscriptionProductsError ->
                handleFetchMobileOnlyPriceError()
            Message.CloseClicked ->
                handleCloseClicked(state)
            is Message.ProductClicked ->
                handleProductClicked(state, message)
            is Message.BuySubscriptionClicked ->
                handleBuySubscriptionClicked(state, message)
            is InternalMessage.MobileOnlySubscriptionPurchaseSuccess ->
                handleMobileOnlySubscriptionPurchaseSuccess(state, message)
            InternalMessage.MobileOnlySubscriptionPurchaseError ->
                handleMobileOnlySubscriptionPurchaseError(state)
            is InternalMessage.SubscriptionSyncSuccess ->
                handleSubscriptionSyncSuccess(state, message)
            InternalMessage.SubscriptionSyncError ->
                handleSubscriptionSyncError(state)
            Message.ClickedTermsOfServiceAndPrivacyPolicy ->
                handleClickedTermsOfServiceAndPrivacyPolicy(state)
            Message.ViewedEventMessage ->
                handleViewedEventMessage(state)
            Message.ScreenShowed ->
                handleScreenShowed(state)
            Message.ScreenHidden ->
                handleScreenHidden(state)
        }

    private fun fetchMobileOnlyPrice(actions: Set<Action> = emptySet()): ReducerResult =
        State.Loading to setOf(InternalAction.FetchMobileOnlyPrice) + actions

    private fun handleFetchMobileOnlyPriceSuccess(
        message: InternalMessage.FetchSubscriptionProductsSuccess
    ): ReducerResult =
        State.Content(
            subscriptionProducts = message.subscriptionProducts,
            selectedProductId = message.subscriptionProducts.first().id
        ) to emptySet()

    private fun handleFetchMobileOnlyPriceError(): ReducerResult =
        State.Error to setOf()

    private fun handleCloseClicked(
        state: State
    ): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                PaywallClickedCloseButtonHyperskillAnalyticEvent(
                    paywallTransitionSource
                )
            ),
            getTargetScreenNavigationAction(paywallTransitionSource)
        )

    private fun handleProductClicked(
        state: State,
        message: Message.ProductClicked
    ): ReducerResult =
        if (state is State.Content) {
            state.copy(
                selectedProductId = message.productId
            ) to setOf(
                InternalAction.LogAnalyticEvent(
                    PaywallClickedProductHyperskillAnalyticEvent(
                        productId = message.productId,
                        paywallTransitionSource = paywallTransitionSource
                    )
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleBuySubscriptionClicked(
        state: State,
        message: Message.BuySubscriptionClicked
    ): ReducerResult =
        if (state is State.Content) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    PaywallClickedBuySubscriptionHyperskillAnalyticEvent(paywallTransitionSource)
                ),
                InternalAction.StartSubscriptionProductPurchase(
                    storeProduct = state.subscriptionProducts.first {
                        it.id == state.selectedProductId
                    }.storeProduct,
                    purchaseParams = message.purchaseParams
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleMobileOnlySubscriptionPurchaseSuccess(
        state: State,
        message: InternalMessage.MobileOnlySubscriptionPurchaseSuccess
    ): ReducerResult =
        if (state is State.Content) {
            when (message.purchaseResult) {
                is PurchaseResult.Succeed -> {
                    state.copy(isPurchaseSyncLoadingShowed = true) to setOf(
                        InternalAction.SyncSubscription,
                        InternalAction.LogAnalyticEvent(
                            PaywallSubscriptionPurchasedAppsFlyerAnalyticEvent,
                            PaywallSubscriptionPurchasedAmplitudeAnalyticEvent
                        )
                    )
                }
                is PurchaseResult.Error.ProductAlreadyPurchasedError -> {
                    state.copy(isPurchaseSyncLoadingShowed = true) to
                        setOf(InternalAction.SyncSubscription)
                }
                PurchaseResult.CancelledByUser -> state to emptySet()
                is PurchaseResult.Error.PaymentPendingError -> {
                    state to setOf(
                        Action.ViewAction.ShowMessage(
                            PaywallFeature.MessageKind.PENDING_PURCHASE
                        ),
                        getTargetScreenNavigationAction(paywallTransitionSource)
                    )
                }

                is PurchaseResult.Error.ErrorWhileFetchingProduct,
                is PurchaseResult.Error.NoProductFound,
                is PurchaseResult.Error.PurchaseNotAllowedError,
                is PurchaseResult.Error.ReceiptAlreadyInUseError,
                is PurchaseResult.Error.StoreProblemError,
                is PurchaseResult.Error.OtherError -> handleMobileOnlySubscriptionPurchaseError(state)
            }
        } else {
            state to emptySet()
        }

    private fun handleMobileOnlySubscriptionPurchaseError(state: State): ReducerResult =
        state to setOf(Action.ViewAction.ShowMessage(PaywallFeature.MessageKind.GENERAL))

    private fun handleSubscriptionSyncSuccess(
        state: State,
        message: InternalMessage.SubscriptionSyncSuccess
    ): ReducerResult =
        if (state is State.Content) {
            state.copy(isPurchaseSyncLoadingShowed = false) to
                if (message.subscription.type == SubscriptionType.MOBILE_ONLY) {
                    setOf(getTargetScreenNavigationAction(paywallTransitionSource))
                } else {
                    setOf(
                        Action.ViewAction.ShowMessage(
                            PaywallFeature.MessageKind.SUBSCRIPTION_WILL_BECOME_AVAILABLE_SOON
                        ),
                        InternalAction.LogWrongSubscriptionTypeAfterSync(
                            expectedSubscriptionType = SubscriptionType.MOBILE_ONLY,
                            actualSubscriptionType = message.subscription.type
                        ),
                        getTargetScreenNavigationAction(paywallTransitionSource)
                    )
                }
        } else {
            state to emptySet()
        }

    private fun handleSubscriptionSyncError(state: State): ReducerResult =
        if (state is State.Content) {
            state.copy(isPurchaseSyncLoadingShowed = false) to setOf(
                Action.ViewAction.ShowMessage(
                    PaywallFeature.MessageKind.SUBSCRIPTION_WILL_BECOME_AVAILABLE_SOON
                ),
                getTargetScreenNavigationAction(paywallTransitionSource)
            )
        } else {
            state to emptySet()
        }

    private fun getTargetScreenNavigationAction(
        paywallTransitionSource: PaywallTransitionSource
    ): Action.ViewAction =
        when (paywallTransitionSource) {
            PaywallTransitionSource.APP_BECOMES_ACTIVE,
            PaywallTransitionSource.MANAGE_SUBSCRIPTION ->
                Action.ViewAction.ClosePaywall
            PaywallTransitionSource.PROFILE_SETTINGS ->
                Action.ViewAction.NavigateTo.BackToProfileSettings
            PaywallTransitionSource.STUDY_PLAN,
            PaywallTransitionSource.TOPIC_COMPLETED_MODAL,
            PaywallTransitionSource.PROBLEMS_LIMIT_MODAL ->
                Action.ViewAction.NavigateTo.Back
        }

    private fun handleClickedTermsOfServiceAndPrivacyPolicy(state: State): ReducerResult =
        if (state is State.Content) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    PaywallClickedTermsOfServiceAndPrivacyPolicyHyperskillAnalyticEvent(paywallTransitionSource)
                ),
                Action.ViewAction.OpenUrl(
                    resourceProvider.getString(SharedResources.strings.paywall_tos_and_privacy_url)
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleViewedEventMessage(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                PaywallViewedHyperskillAnalyticEvent(paywallTransitionSource)
            )
        )

    private fun handleScreenShowed(state: State): ReducerResult =
        state to setOf(Action.ViewAction.NotifyPaywallIsShown(isPaywallShown = true))

    private fun handleScreenHidden(state: State): ReducerResult =
        state to setOf(Action.ViewAction.NotifyPaywallIsShown(isPaywallShown = false))
}