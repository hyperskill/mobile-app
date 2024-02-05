package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.paywall.domain.analytic.PaywallClickedBuySubscriptionHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedContinueWithLimitsHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedRetryContentLoadingHyperskillAnalyticEvent
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

class PaywallReducer(
    private val paywallTransitionSource: PaywallTransitionSource
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.Initialize -> fetchMobileOnlyPrice()
            Message.RetryContentLoading ->
                fetchMobileOnlyPrice(
                    setOf(
                        InternalAction.LogAnalyticsEvent(
                            PaywallClickedRetryContentLoadingHyperskillAnalyticEvent(
                                paywallTransitionSource
                            )
                        )
                    )
                )
            Message.ViewedEventMessage -> handleViewedEventMessage(state)
            is Message.BuySubscriptionClicked -> handleBuySubscriptionClicked(state, message)
            Message.ContinueWithLimitsClicked -> handleContinueWithLimitsClicked(state)
            is InternalMessage.FetchMobileOnlyPriceSuccess ->
                handleFetchMobileOnlyPriceSuccess(message)
            InternalMessage.FetchMobileOnlyPriceError ->
                handleFetchMobileOnlyPriceError()
            is InternalMessage.MobileOnlySubscriptionPurchaseSuccess ->
                handleMobileOnlySubscriptionPurchaseSuccess(state, message)
            InternalMessage.MobileOnlySubscriptionPurchaseError ->
                handlePurchaseError(state)
            is InternalMessage.SubscriptionSyncSuccess ->
                handleSubscriptionSyncSuccess(state, message)
            InternalMessage.SubscriptionSyncError ->
                handleSubscriptionSyncError(state)
        }

    private fun fetchMobileOnlyPrice(
        actions: Set<Action> = emptySet()
    ): ReducerResult =
        State.Loading to
            setOf(InternalAction.FetchMobileOnlyPrice) + actions

    private fun handleViewedEventMessage(
        state: State
    ): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                PaywallViewedHyperskillAnalyticEvent(
                    paywallTransitionSource
                )
            )
        )

    private fun handleBuySubscriptionClicked(
        state: State,
        message: Message.BuySubscriptionClicked
    ): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                PaywallClickedBuySubscriptionHyperskillAnalyticEvent(
                    paywallTransitionSource
                )
            ),
            InternalAction.StartMobileOnlySubscriptionPurchase(message.purchaseParams)
        )

    private fun handleContinueWithLimitsClicked(
        state: State
    ): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                PaywallClickedContinueWithLimitsHyperskillAnalyticEvent(
                    paywallTransitionSource
                )
            ),
            Action.ViewAction.CompletePaywall
        )

    private fun handleFetchMobileOnlyPriceSuccess(
        message: InternalMessage.FetchMobileOnlyPriceSuccess
    ): ReducerResult =
        State.Content(message.formattedPrice) to emptySet()

    private fun handleFetchMobileOnlyPriceError(): ReducerResult =
        State.Error to setOf()

    private fun handleMobileOnlySubscriptionPurchaseSuccess(
        state: State,
        message: InternalMessage.MobileOnlySubscriptionPurchaseSuccess
    ): ReducerResult =
        if (state is State.Content) {
            when (message.purchaseResult) {
                is PurchaseResult.Succeed,
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
                        Action.ViewAction.CompletePaywall
                    )
                }

                is PurchaseResult.Error.ErrorWhileFetchingProduct,
                is PurchaseResult.Error.NoProductFound,
                is PurchaseResult.Error.PurchaseNotAllowedError,
                is PurchaseResult.Error.ReceiptAlreadyInUseError,
                is PurchaseResult.Error.StoreProblemError,
                is PurchaseResult.Error.OtherError -> handlePurchaseError(state)
            }
        } else {
            state to emptySet()
        }

    private fun handlePurchaseError(
        state: State
    ): ReducerResult =
        state to setOf(
            Action.ViewAction.ShowMessage(
                PaywallFeature.MessageKind.GENERAL
            )
        )

    private fun handleSubscriptionSyncSuccess(
        state: State,
        message: InternalMessage.SubscriptionSyncSuccess
    ): ReducerResult =
        if (state is State.Content) {
            state.copy(isPurchaseSyncLoadingShowed = false) to
                if (message.subscription.type == SubscriptionType.MOBILE_ONLY) {
                    setOf(Action.ViewAction.CompletePaywall)
                } else {
                    setOf(
                        Action.ViewAction.ShowMessage(
                            PaywallFeature.MessageKind.SUBSCRIPTION_WILL_BECOME_AVAILABLE_SOON
                        ),
                        InternalAction.LogWrongSubscriptionTypeAfterSync(
                            expectedSubscriptionType = SubscriptionType.MOBILE_ONLY,
                            actualSubscriptionType = message.subscription.type
                        )
                    )
                }
        } else {
            state to emptySet()
        }

    private fun handleSubscriptionSyncError(
        state: State
    ): ReducerResult =
        if (state is State.Content) {
            state.copy(isPurchaseSyncLoadingShowed = false) to setOf(
                Action.ViewAction.ShowMessage(
                    PaywallFeature.MessageKind.SUBSCRIPTION_WILL_BECOME_AVAILABLE_SOON
                ),
                Action.ViewAction.CompletePaywall
            )
        } else {
            state to emptySet()
        }
}