package org.hyperskill.app.problems_limit.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Action
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Message
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.State
import org.hyperskill.app.subscriptions.domain.model.Subscription
import ru.nobird.app.presentation.redux.reducer.StateReducer
import kotlin.time.Duration

internal class ProblemsLimitReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle || message.forceUpdate) {
                    State.Loading to setOf(Action.LoadSubscription(message.forceUpdate))
                } else {
                    null
                }
            Message.SubscriptionLoadingResult.Error ->
                State.NetworkError to emptySet()
            is Message.SubscriptionLoadingResult.Success -> {
                val updateIn = calculateUpdateInDuration(message.subscription)

                State.Content(message.subscription, updateIn) to buildSet {
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
            is Message.AppLaunchedFromBackground ->
                state to setOf(Action.ResetCurrentSubscriptionRepository)
        } ?: (state to emptySet())

    private fun calculateUpdateInDuration(subscription: Subscription): Duration? =
        subscription.stepsLimitResetTime?.let {
            it - Clock.System.now()
        }
}