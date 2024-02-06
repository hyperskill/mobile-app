package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.manage_subscription.domain.analytics.ManageSubscriptionHyperskillAnalyticsEvent
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.InternalAction
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.InternalMessage
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class ManageSubscriptionReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.Initialize -> handleInitialize(state)
            Message.ViewedEventMessage -> handleViewedEventMessage(state)
            is InternalMessage.FetchSubscriptionSuccess -> handleFetchSubscriptionSuccess(message)
            InternalMessage.FetchSubscriptionError -> handleFetchSubscriptionError()
            Message.RetryContentLoading -> fetchSubscription(forceLoadFromNetwork = true)
        }

    private fun handleInitialize(state: State): ReducerResult =
        if (state is State.Idle) {
            fetchSubscription(forceLoadFromNetwork = false)
        } else {
            state to emptySet()
        }

    private fun handleViewedEventMessage(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticsEvent(
                ManageSubscriptionHyperskillAnalyticsEvent
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

    private fun fetchSubscription(forceLoadFromNetwork: Boolean): ReducerResult =
        State.Loading to setOf(InternalAction.FetchSubscription(forceLoadFromNetwork))
}