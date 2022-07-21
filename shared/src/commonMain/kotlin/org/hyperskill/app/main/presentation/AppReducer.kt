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
            is Message.Init -> {
                if (state is State.Idle || (state is State.NetworkError && message.forceUpdate)) {
                    State.Loading to setOf(Action.DetermineUserAccountStatus)
                } else {
                    null
                }
            }
            is Message.UserAuthorized ->
                if (state is State.Ready && !state.isAuthorized) {
                    val action = if (message.isNewUser) {
                        Action.ViewAction.NavigateTo.NewUserScreen
                    } else {
                        Action.ViewAction.NavigateTo.HomeScreen
                    }
                    State.Ready(isAuthorized = true) to setOf(action)
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
                    val isAuthorized = !message.profile.isGuest

                    println("ALT – ${message.profile}")

                    val action =
                        if (isAuthorized) {
                            if (message.profile.trackId == null) {
                                Action.ViewAction.NavigateTo.NewUserScreen
                            } else {
                                Action.ViewAction.NavigateTo.HomeScreen
                            }
                        } else {
                            Action.ViewAction.NavigateTo.AuthScreen
                        }

                    State.Ready(isAuthorized = isAuthorized) to setOf(action)
                } else {
                    null
                }
            is Message.UserAccountStatusError ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
        } ?: (state to emptySet())
}