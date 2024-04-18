package org.hyperskill.app.step_quiz_toolbar.presentation

import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.Action
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.Message
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias StepQuizToolbarReducerResult = Pair<State, Set<Action>>

class StepQuizToolbarReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): StepQuizToolbarReducerResult =
        when (message) {
            Message.Initialize -> handleInitialization(state)
            is StepQuizToolbarFeature.InternalMessage.SubscriptionFetchSuccess ->
                handleSubscriptionFetchSuccess(message)
            StepQuizToolbarFeature.InternalMessage.SubscriptionFetchError -> handleSubscriptionFetchError()
            is StepQuizToolbarFeature.InternalMessage.SubscriptionChanged -> handleSubscriptionChanged(state, message)
            Message.ProblemsLimitClicked -> handleProblemsLimitClicked(state)
        }

    private fun handleInitialization(state: State): StepQuizToolbarReducerResult =
        if (state is State.Idle) {
            State.Loading to setOf(StepQuizToolbarFeature.InternalAction.FetchSubscription)
        } else {
            state to emptySet()
        }

    private fun handleSubscriptionFetchSuccess(
        message: StepQuizToolbarFeature.InternalMessage.SubscriptionFetchSuccess
    ): StepQuizToolbarReducerResult =
        State.Content(message.subscription) to emptySet()

    private fun handleSubscriptionFetchError(): StepQuizToolbarReducerResult =
        State.Error to emptySet()

    private fun handleSubscriptionChanged(
        state: State,
        message: StepQuizToolbarFeature.InternalMessage.SubscriptionChanged
    ): StepQuizToolbarReducerResult =
        if (state is State.Content) {
            state.copy(subscription = message.subscription) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleProblemsLimitClicked(state: State): StepQuizToolbarReducerResult =
        state to setOf()
}