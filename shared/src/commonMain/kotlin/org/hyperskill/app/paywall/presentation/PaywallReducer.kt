package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.paywall.domain.analytic.PaywallClickedBuySubscriptionHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallClickedContinueWithLimitsHyperskillAnalyticEvent
import org.hyperskill.app.paywall.domain.analytic.PaywallViewedHyperskillAnalyticEvent
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias PaywallReducerResult = Pair<State, Set<Action>>

class PaywallReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): PaywallReducerResult =
        when (message) {
            Message.ViewedEventMessage -> handleViewedEventMessage(state)
            Message.BuySubscriptionClicked -> handleBuySubscriptionClicked(state)
            Message.ContinueWithLimitsClicked -> handleContinueWithLimitsClicked(state)
        }

    private fun handleViewedEventMessage(
        state: State
    ): PaywallReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                PaywallViewedHyperskillAnalyticEvent
            )
        )

    private fun handleBuySubscriptionClicked(
        state: State
    ): PaywallReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                PaywallClickedBuySubscriptionHyperskillAnalyticEvent
            )
        )

    private fun handleContinueWithLimitsClicked(
        state: State
    ): PaywallReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                PaywallClickedContinueWithLimitsHyperskillAnalyticEvent
            )
        )
}