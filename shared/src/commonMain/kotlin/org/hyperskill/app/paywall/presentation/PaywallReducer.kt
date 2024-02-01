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
                handlePurchaseError(state)
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
        state.updateContent { content ->
            when (message.purchaseResult) {
                is PurchaseResult.Succeed -> {
                    content.copy(isPurchaseSyncLoadingShowed = true) to
                        setOf(InternalAction.SyncSubscription)
                }
                PurchaseResult.CancelledByUser -> content to emptySet()

                is PurchaseResult.Error.ErrorWhileFetchingProduct,
                is PurchaseResult.Error.NoProductFound,
                is PurchaseResult.Error.PaymentPendingError,
                is PurchaseResult.Error.ProductAlreadyPurchasedError,
                is PurchaseResult.Error.PurchaseNotAllowedError,
                is PurchaseResult.Error.ReceiptAlreadyInUseError,
                is PurchaseResult.Error.StoreProblemError,
                is PurchaseResult.Error.OtherError -> TODO("Not implemented")
            }
        }

    private fun handleSubscriptionSyncSuccess(
        state: State,
        message: InternalMessage.SubscriptionSyncSuccess
    ): ReducerResult =
        state.updateContent { content ->
            content.copy(isPurchaseSyncLoadingShowed = false) to
                setOf(
                    if (message.subscription.type == SubscriptionType.MOBILE_ONLY) {
                        Action.ViewAction.CompletePaywall
                    } else {
                        Action.ViewAction.ShowPurchaseError
                    }
                )
        }

    private fun handlePurchaseError(
        state: State
    ): ReducerResult =
        state to setOf(Action.ViewAction.ShowPurchaseError)
}

private fun State.updateContent(block: (State.Content) -> ReducerResult): ReducerResult =
    if (this is State.Content) {
        block(this)
    } else {
        this to emptySet()
    }