package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.manage_subscription.domain.analytic.ManageSubscriptionClickedManageHyperskillAnalyticEvent
import org.hyperskill.app.manage_subscription.domain.analytic.ManageSubscriptionViewedHyperskillAnalyticEvent
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.InternalAction
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.InternalMessage
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
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
            Message.ManageSubscriptionClicked -> handleManageSubscriptionClicked(state)
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

    private fun handleManageSubscriptionClicked(state: State): ReducerResult =
        if (state is State.Content && state.manageSubscriptionUrl != null) {
            state to setOf(
                InternalAction.LogAnalyticsEvent(ManageSubscriptionClickedManageHyperskillAnalyticEvent),
                Action.ViewAction.OpenUrl(state.manageSubscriptionUrl)
            )
        } else {
            state to emptySet()
        }
}