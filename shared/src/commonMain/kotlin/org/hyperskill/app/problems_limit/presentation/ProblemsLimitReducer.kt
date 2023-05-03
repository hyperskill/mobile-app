package org.hyperskill.app.problems_limit.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Action
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Message
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.State
import org.hyperskill.app.subscriptions.domain.model.Subscription
import ru.nobird.app.presentation.redux.reducer.StateReducer
import kotlin.time.Duration

class ProblemsLimitReducer(private val screen: ProblemsLimitScreen) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle || message.forceUpdate) {
                    State.Loading to setOf(
                        Action.LoadSubscription(screen = screen, forceUpdate = message.forceUpdate)
                    )
                } else {
                    null
                }
            Message.SubscriptionLoadingResult.Error ->
                State.NetworkError to emptySet()
            is Message.SubscriptionLoadingResult.Success -> {
                val updateIn = calculateUpdateInDuration(message.subscription)

                State.Content(message.subscription, message.isFreemiumEnabled, updateIn) to buildSet {
                    if (updateIn != null) {
                        add(Action.LaunchTimer(updateIn))
                    }
                }
            }
            is Message.UpdateInChanged ->
                if (state is State.Content) {
                    state.copy(updateIn = message.newUpdateIn) to emptySet()
                } else {
                    null
                }
            is Message.SubscriptionChanged ->
                if (state is State.Content) {
                    val updateIn = calculateUpdateInDuration(message.newSubscription)

                    state.copy(subscription = message.newSubscription) to buildSet {
                        if (updateIn != null) {
                            add(Action.LaunchTimer(updateIn))
                        }
                    }
                } else {
                    null
                }
            is Message.PullToRefresh ->
                when (state) {
                    is State.Content ->
                        if (state.isRefreshing) null
                        else state.copy(isRefreshing = true) to setOf(
                            Action.LoadSubscription(screen = screen, forceUpdate = true)
                        )
                    is State.NetworkError ->
                        State.Loading to setOf(
                            Action.LoadSubscription(screen = screen, forceUpdate = false)
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