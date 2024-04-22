package org.hyperskill.app.problems_limit.presentation

import org.hyperskill.app.problems_limit.domain.analytic.StepQuizToolbarLimitClickedHyperskillAnalyticEvent
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Action
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.InternalAction
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.InternalMessage
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Message
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.State
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalLaunchSource
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProblemsLimitReducerResult = Pair<State, Set<Action>>

class ProblemsLimitReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProblemsLimitReducerResult =
        when (message) {
            InternalMessage.Initialize -> handleInitialization(state)
            is InternalMessage.SubscriptionFetchSuccess -> handleSubscriptionFetchSuccess(message)
            InternalMessage.SubscriptionFetchError -> handleSubscriptionFetchError()
            is InternalMessage.SubscriptionChanged -> handleSubscriptionChanged(state, message)
            Message.ProblemsLimitClicked -> handleProblemsLimitClicked(state)
        }

    private fun handleInitialization(state: State): ProblemsLimitReducerResult =
        if (state is State.Idle) {
            State.Loading to setOf(InternalAction.FetchSubscription)
        } else {
            state to emptySet()
        }

    private fun handleSubscriptionFetchSuccess(
        message: InternalMessage.SubscriptionFetchSuccess
    ): ProblemsLimitReducerResult =
        State.Content(message.subscription, message.chargeLimitsStrategy) to emptySet()

    private fun handleSubscriptionFetchError(): ProblemsLimitReducerResult =
        State.Error to emptySet()

    private fun handleSubscriptionChanged(
        state: State,
        message: InternalMessage.SubscriptionChanged
    ): ProblemsLimitReducerResult =
        if (state is State.Content) {
            state.copy(subscription = message.subscription) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleProblemsLimitClicked(state: State): ProblemsLimitReducerResult =
        if (state is State.Content) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    StepQuizToolbarLimitClickedHyperskillAnalyticEvent(stepRoute.analyticRoute)
                ),
                Action.ViewAction.ShowProblemsLimitInfoModal(
                    subscription = state.subscription,
                    chargeLimitsStrategy = state.chargeLimitsStrategy,
                    context = ProblemsLimitInfoModalContext.Step(
                        launchSource = ProblemsLimitInfoModalLaunchSource.USER_INITIATED,
                        stepRoute = stepRoute
                    )
                )
            )
        } else {
            state to emptySet()
        }
}