package org.hyperskill.app.step_quiz_toolbar.presentation

import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalLaunchSource
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_toolbar.domain.analytic.StepQuizToolbarLimitClickedHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.Action
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.InternalAction
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.InternalMessage
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.Message
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias StepQuizToolbarReducerResult = Pair<State, Set<Action>>

class StepQuizToolbarReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): StepQuizToolbarReducerResult =
        when (message) {
            InternalMessage.Initialize -> handleInitialization(state)
            is InternalMessage.SubscriptionFetchSuccess -> handleSubscriptionFetchSuccess(message)
            InternalMessage.SubscriptionFetchError -> handleSubscriptionFetchError()
            is InternalMessage.SubscriptionChanged -> handleSubscriptionChanged(state, message)
            Message.ProblemsLimitClicked -> handleProblemsLimitClicked(state)
        }

    private fun handleInitialization(state: State): StepQuizToolbarReducerResult =
        when {
            state is State.Unavailable || !StepQuizToolbarResolver.isToolbarAvailable(stepRoute) ->
                State.Unavailable to emptySet()
            state is State.Idle ->
                State.Loading to setOf(InternalAction.FetchSubscription)
            else ->
                state to emptySet()
        }

    private fun handleSubscriptionFetchSuccess(
        message: InternalMessage.SubscriptionFetchSuccess
    ): StepQuizToolbarReducerResult =
        State.Content(message.subscription, message.chargeLimitsStrategy) to emptySet()

    private fun handleSubscriptionFetchError(): StepQuizToolbarReducerResult =
        State.Error to emptySet()

    private fun handleSubscriptionChanged(
        state: State,
        message: InternalMessage.SubscriptionChanged
    ): StepQuizToolbarReducerResult =
        if (state is State.Content) {
            state.copy(subscription = message.subscription) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleProblemsLimitClicked(state: State): StepQuizToolbarReducerResult =
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