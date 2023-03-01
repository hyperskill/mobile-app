package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class StageImplementReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> {
        TODO("Not yet implemented")
    }
}