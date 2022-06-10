package org.hyperskill.app.step.presentation

import ru.nobird.app.presentation.redux.reducer.StateReducer
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.State

class StepReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Init ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Data || state is State.Error))
                ) {
                    State.Loading to setOf(Action.FetchStep(message.stepId))
                } else {
                    null
                }
            is Message.StepLoaded.Success ->
                State.Data(message.step) to emptySet()
            is Message.StepLoaded.Error ->
                State.Error to emptySet()
        } ?: (state to emptySet())
}