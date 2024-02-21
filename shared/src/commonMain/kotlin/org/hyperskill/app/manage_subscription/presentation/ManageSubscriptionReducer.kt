package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.manage_subscription.domain.analytic.ManageSubscriptionClickedManageHyperskillAnalyticEvent
import org.hyperskill.app.manage_subscription.domain.analytic.ManageSubscriptionViewedHyperskillAnalyticEvent
import org.hyperskill.app.manage_subscription.domain.analytic.RenewSubscriptionClickedManageHyperskillAnalyticEvent
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.InternalAction
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.InternalMessage
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.subscriptions.domain.model.isActive
import org.hyperskill.app.subscriptions.domain.model.isExpired
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class ManageSubscriptionReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.Initialize -> handleInitialize(state)
            Message.ViewedEventMessage -> handleViewedEventMessage(state)
            is InternalMessage.FetchSubscriptionSuccess -> handleFetchSubscriptionSuccess(message)
            InternalMessage.FetchSubscriptionError -> handleFetchSubscriptionError()
            Message.RetryContentLoading -> fetchSubscription()
            Message.ActionButtonClicked -> handleActionButtonClicked(state)
            is InternalMessage.SubscriptionChanged -> handleSubscriptionChanged(state, message)
        }

    private fun handleInitialize(state: State): ReducerResult =
        if (state is State.Idle) {
            fetchSubscription()
        } else {
            state to emptySet()
        }

    private fun handleViewedEventMessage(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                ManageSubscriptionViewedHyperskillAnalyticEvent
            )
        )

    private fun handleFetchSubscriptionSuccess(
        message: InternalMessage.FetchSubscriptionSuccess
    ): ReducerResult =
        State.Content(
            subscription = message.subscription,
            manageSubscriptionUrl = message.manageSubscriptionUrl
        ) to emptySet()

    private fun handleFetchSubscriptionError(): ReducerResult =
        State.Error to emptySet()

    private fun fetchSubscription(): ReducerResult =
        State.Loading to setOf(InternalAction.FetchSubscription)

    private fun handleActionButtonClicked(state: State): ReducerResult =
        if (state is State.Content) {
            state to when  {
                state.subscription.isActive && state.manageSubscriptionUrl != null -> {
                    setOf(
                        InternalAction.LogAnalyticsEvent(ManageSubscriptionClickedManageHyperskillAnalyticEvent),
                        Action.ViewAction.OpenUrl(state.manageSubscriptionUrl)
                    )
                }
                state.subscription.isExpired -> {
                    setOf(
                        InternalAction.LogAnalyticsEvent(RenewSubscriptionClickedManageHyperskillAnalyticEvent),
                        Action.ViewAction.NavigateTo.Paywall(PaywallTransitionSource.MANAGE_SUBSCRIPTION)
                    )
                }
                else -> emptySet()
            }
        } else {
            state to emptySet()
        }

    private fun handleSubscriptionChanged(
        state: State,
        message: InternalMessage.SubscriptionChanged
    ): ReducerResult =
        if (state is State.Content) {
            State.Content(
                subscription = message.subscription,
                manageSubscriptionUrl = message.manageSubscriptionUrl
            ) to setOf()
        } else {
            state to emptySet()
        }
}