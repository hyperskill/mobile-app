package org.hyperskill.app.app.presentation

import org.hyperskill.app.app.presentation.AppFeature.Action
import org.hyperskill.app.app.presentation.AppFeature.Message
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AppReducer : StateReducer<Unit, Message, Action> {
    override fun reduce(
        state: Unit,
        message: Message
    ): Pair<Unit, Set<Action>> =
        when (message) {
            is Message.AppStarted ->
                Unit to setOf(Action.DetermineUserAccountStatus)
            Message.UserAuthorized ->
                Unit to setOf(Action.ViewAction.NavigateTo.HomeScreen)
            is Message.UserDeauthorized ->
                Unit to setOf(Action.ViewAction.NavigateTo.AuthScreen)
        }
}