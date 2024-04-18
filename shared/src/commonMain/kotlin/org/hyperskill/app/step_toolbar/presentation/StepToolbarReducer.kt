package org.hyperskill.app.step_toolbar.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.Action
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.InternalAction
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.InternalMessage
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.Message
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias StepToolbarReducerResult = Pair<State, Set<Action>>

class StepToolbarReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is InternalMessage.Initialize -> handleInitialize(state, message)
            InternalMessage.FetchTopicProgressError -> handleFetchTopicProgressError(state)
            is InternalMessage.FetchTopicProgressSuccess -> handleFetchTopicProgressSuccess(state, message)
        } ?: (state to emptySet())

    private fun handleInitialize(
        state: State,
        message: InternalMessage.Initialize
    ): StepToolbarReducerResult? =
        if (state is State.Unavailable || !StepToolbarResolver.isProgressBarAvailable(stepRoute)) {
            State.Unavailable to emptySet()
        } else {
            message.topicId?.let { topicId ->
                val shouldReload = state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))

                if (shouldReload) {
                    State.Loading to setOf(InternalAction.FetchTopicProgress(topicId, message.forceUpdate))
                } else {
                    null
                }
            }
        }

    private fun handleFetchTopicProgressError(state: State): StepToolbarReducerResult? =
        if (state is State.Loading) {
            State.Error to emptySet()
        } else {
            null
        }

    private fun handleFetchTopicProgressSuccess(
        state: State,
        message: InternalMessage.FetchTopicProgressSuccess
    ): StepToolbarReducerResult? =
        if (state is State.Loading) {
            State.Content(message.topicProgress) to emptySet()
        } else {
            null
        }
}