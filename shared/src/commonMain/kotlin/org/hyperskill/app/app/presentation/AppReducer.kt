package org.hyperskill.app.app.presentation

import org.hyperskill.app.app.presentation.AppFeature.Action
import org.hyperskill.app.app.presentation.AppFeature.Message
import org.hyperskill.app.app.presentation.AppFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AppReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): Pair<State, Set<Action>> =
        when (message) {
            is Message.AppStarted ->
                State.Idle to setOf(Action.DetermineUserAccountStatus)
            Message.UserAuthorized ->
                State.Idle to setOf(Action.ViewAction.NavigateTo.HomeScreen)
            is Message.UserDeauthorized ->
                State.Idle to setOf(Action.ViewAction.NavigateTo.AuthScreen)
        }
}