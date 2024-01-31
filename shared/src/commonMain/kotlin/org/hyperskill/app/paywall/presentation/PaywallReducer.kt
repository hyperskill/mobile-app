package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.paywall.domain.analytic.PaywallClickedBuySubscriptionHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedContinueWithLimitsHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallViewedHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
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
            Message.BuySubscriptionClicked -> handleBuySubscriptionClicked(state)
            Message.ContinueWithLimitsClicked -> handleContinueWithLimitsClicked(state)
            is PaywallFeature.InternalMessage.FetchMobileOnlyPriceSuccess ->
                handleFetchMobileOnlyPriceSuccess(message)
            PaywallFeature.InternalMessage.FetchMobileOnlyPriceError ->
                handleFetchMobileOnlyPriceError()
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
        state: State
    ): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                PaywallClickedBuySubscriptionHyperskillAnalyticEvent(
                    paywallTransitionSource
                )
            )
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
        message: PaywallFeature.InternalMessage.FetchMobileOnlyPriceSuccess
    ): ReducerResult =
        State.Content(message.formattedPrice) to emptySet()

    private fun handleFetchMobileOnlyPriceError(): ReducerResult =
        State.Error to setOf()
}