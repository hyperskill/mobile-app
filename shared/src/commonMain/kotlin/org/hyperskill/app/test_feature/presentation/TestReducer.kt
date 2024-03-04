package org.hyperskill.app.test_feature

import org.hyperskill.app.test_feature.TestFeature.Action
import org.hyperskill.app.test_feature.TestFeature.InternalAction
import org.hyperskill.app.test_feature.TestFeature.Message
import org.hyperskill.app.test_feature.TestFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias TestReducerResult = Pair<State, Set<Action>>

class TestReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): TestReducerResult =
        when (message) {
        }
}