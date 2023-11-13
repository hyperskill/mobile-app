package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Action
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Message
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ChallengeWidgetReducerResult = Pair<State, Set<Action>>

class ChallengeWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ChallengeWidgetReducerResult =
        TODO()
}