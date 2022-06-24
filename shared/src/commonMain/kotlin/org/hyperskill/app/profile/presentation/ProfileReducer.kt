package org.hyperskill.app.profile.presentation

import ru.nobird.app.presentation.redux.reducer.StateReducer
import org.hyperskill.app.profile.presentation.ProfileFeature.State
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message

class ProfileReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Init -> {
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))
                ) {
                    if (message.isInitCurrent) {
                        State.Loading to setOf(Action.FetchCurrentProfile)
                    } else {
                        State.Loading to setOf(Action.FetchProfile(message.profileId!!))
                    }
                } else {
                    null
                }
            }
            is Message.ProfileLoaded.Success ->
                State.Content(message.profile) to emptySet()
            is Message.ProfileLoaded.Error ->
                State.Error to emptySet()
        } ?: (state to emptySet())
}