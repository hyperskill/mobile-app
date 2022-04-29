package org.hyperskill.app.main.presentation

import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.main.presentation.AppFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class AppReducer : StateReducer<State, Message, Action> {
    override fun reduce(
        state: State,
        message: Message
    ): Pair<State, Set<Action>> =
        when (message) {
            is Message.AppStarted -> {
                if (state is State.Idle) {
                    State.Loading to setOf(Action.DetermineUserAccountStatus)
                } else {
                    null
                }
            }
            is Message.UserAuthorized ->
                if (state is State.Ready && !state.isAuthorized) {
                    State.Ready(isAuthorized = true) to setOf(Action.ViewAction.NavigateTo.HomeScreen)
                } else {
                    null
                }
            is Message.UserDeauthorized ->
                if (state is State.Ready && state.isAuthorized) {
                    State.Ready(isAuthorized = false) to setOf(Action.ViewAction.NavigateTo.AuthScreen)
                } else {
                    null
                }
            is Message.UserAccountStatus ->
                if (state is State.Loading) {
                    val action = if (message.isAuthorized) {
                        Action.ViewAction.NavigateTo.HomeScreen
                    } else {
                        Action.ViewAction.NavigateTo.AuthScreen
                    }
                    State.Ready(isAuthorized = message.isAuthorized) to setOf(action)
                } else {
                    null
                }
        } ?: state to emptySet()
}