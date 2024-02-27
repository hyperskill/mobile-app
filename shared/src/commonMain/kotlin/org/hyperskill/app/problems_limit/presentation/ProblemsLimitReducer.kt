package org.hyperskill.app.problems_limit.presentation

import kotlin.time.Duration
import kotlinx.datetime.Clock
import org.hyperskill.app.problems_limit.domain.analytic.ProblemsLimitClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Action
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.InternalAction
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.InternalMessage
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Message
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.State
import org.hyperskill.app.subscriptions.domain.model.Subscription
import ru.nobird.app.presentation.redux.reducer.StateReducer

class ProblemsLimitReducer(private val screen: ProblemsLimitScreen) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is InternalMessage.Initialize ->
                if (state is State.Idle || message.forceUpdate) {
                    State.Loading to setOf(
                        InternalAction.LoadSubscription(screen = screen, forceUpdate = message.forceUpdate)
                    )
                } else {
                    null
                }
            Message.RetryContentLoading ->
                if (state is State.NetworkError) {
                    State.Loading to setOf(
                        InternalAction.LoadSubscription(screen = screen, forceUpdate = true),
                        InternalAction.LogAnalyticEvent(
                            ProblemsLimitClickedRetryContentLoadingHyperskillAnalyticEvent(screen)
                        )
                    )
                } else {
                    null
                }
            InternalMessage.LoadSubscriptionResultError ->
                State.NetworkError to emptySet()
            is InternalMessage.LoadSubscriptionResultSuccess -> {
                val updateIn = calculateUpdateInDuration(message.subscription)

                State.Content(
                    subscription = message.subscription,
                    isFreemiumEnabled = message.isFreemiumEnabled,
                    isFreemiumWrongSubmissionChargeLimitsEnabled = message.isFreemiumWrongSubmissionChargeLimitsEnabled,
                    updateIn = updateIn
                ) to buildSet {
                    if (updateIn != null) {
                        add(InternalAction.LaunchTimer(updateIn))
                    }
                }
            }
            is InternalMessage.UpdateInChanged ->
                if (state is State.Content) {
                    state.copy(updateIn = message.newUpdateIn) to emptySet()
                } else {
                    null
                }
            is InternalMessage.SubscriptionChanged ->
                if (state is State.Content) {
                    val updateIn = calculateUpdateInDuration(message.newSubscription)

                    state.copy(subscription = message.newSubscription) to buildSet {
                        if (updateIn != null) {
                            add(InternalAction.LaunchTimer(updateIn))
                        }
                    }
                } else {
                    null
                }
            is InternalMessage.PullToRefresh ->
                when (state) {
                    is State.Content ->
                        if (state.isRefreshing) {
                            null
                        } else state.copy(isRefreshing = true) to setOf(
                            InternalAction.LoadSubscription(screen = screen, forceUpdate = true)
                        )
                    is State.NetworkError ->
                        State.Loading to setOf(
                            InternalAction.LoadSubscription(screen = screen, forceUpdate = false)
                        )
                    else ->
                        null
                }
        } ?: (state to emptySet())

    private fun calculateUpdateInDuration(subscription: Subscription): Duration? =
        subscription.stepsLimitResetTime?.let {
            it - Clock.System.now()
        }
}