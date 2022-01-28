package org.hyperskill.app.login.presentation

import org.hyperskill.app.login.presentation.UserLoginFeature.State
import org.hyperskill.app.login.presentation.UserLoginFeature.Message
import org.hyperskill.app.login.presentation.UserLoginFeature.Action
import ru.nobird.app.presentation.redux.reducer.StateReducer

class UserLoginReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> {
        TODO("Not yet implemented")
    }
}