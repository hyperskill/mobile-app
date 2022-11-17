package org.hyperskill.app.topics_repetitions.presentation

import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.State
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import ru.nobird.app.presentation.redux.reducer.StateReducer

class TopicsRepetitionsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.Initialize)
                } else {
                    null
                }
        } ?: (state to emptySet())
}