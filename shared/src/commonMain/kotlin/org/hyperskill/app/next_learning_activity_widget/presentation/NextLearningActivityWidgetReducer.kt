package org.hyperskill.app.next_learning_activity_widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.Action
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.InternalAction
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.InternalMessage
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.Message
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias NextLearningActivityWidgetReducerResult = Pair<State, Set<Action>>

class NextLearningActivityWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): NextLearningActivityWidgetReducerResult =
        when (message) {
            is InternalMessage.Initialize -> {
                handleInitialize(state, message)
            }
            InternalMessage.FetchNextLearningActivityError -> {
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            }
            is InternalMessage.FetchNextLearningActivitySuccess -> {
                handleFetchNextLearningActivitySuccess(state, message)
            }
            is InternalMessage.NextLearningActivityChanged -> {
                mapLearningActivityToState(message.learningActivity) to emptySet()
            }
            Message.NextLearningActivityClicked -> TODO()
            Message.RetryContentLoading -> {
                if (state is State.NetworkError) {
                    State.Loading to setOf(InternalAction.FetchNextLearningActivity(forceUpdate = true))
                } else {
                    null
                }
            }
            InternalMessage.PullToRefresh -> {
                handlePullToRefresh(state)
            }
        } ?: (state to emptySet())

    private fun handleInitialize(
        state: State,
        message: InternalMessage.Initialize
    ): NextLearningActivityWidgetReducerResult? =
        if (state is State.Idle ||
            (message.forceUpdate && (state is State.Empty || state is State.Content || state is State.NetworkError))
        ) {
            State.Loading to setOf(InternalAction.FetchNextLearningActivity(forceUpdate = message.forceUpdate))
        } else {
            null
        }

    private fun handleFetchNextLearningActivitySuccess(
        state: State,
        message: InternalMessage.FetchNextLearningActivitySuccess
    ): NextLearningActivityWidgetReducerResult? =
        if (state is State.Loading) {
            mapLearningActivityToState(message.learningActivity) to emptySet()
        } else {
            null
        }

    private fun handlePullToRefresh(state: State): NextLearningActivityWidgetReducerResult? =
        when (state) {
            is State.Content ->
                if (state.isRefreshing) {
                    null
                } else {
                    state.copy(isRefreshing = true) to setOf(
                        InternalAction.FetchNextLearningActivity(forceUpdate = true)
                    )
                }
            is State.NetworkError, State.Empty ->
                State.Loading to setOf(InternalAction.FetchNextLearningActivity(forceUpdate = true))
            else ->
                null
        }

    private fun mapLearningActivityToState(learningActivity: LearningActivity?): State =
        if (learningActivity != null) {
            State.Content(learningActivity)
        } else {
            State.Empty
        }
}