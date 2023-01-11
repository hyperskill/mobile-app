package org.hyperskill.app.debug.presentation

import org.hyperskill.app.debug.presentation.DebugFeature.Action
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import org.hyperskill.app.debug.presentation.DebugFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class DebugReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                if (state is State.Idle || state is State.Error && message.forceUpdate) {
                    State.Loading to setOf(Action.FetchDebugSettings)
                } else {
                    null
                }
            }
            is Message.FetchDebugSettingsSuccess -> {
                if (state is State.Loading) {
                    State.Content(message.debugSettings.endpointConfigType) to emptySet()
                } else {
                    null
                }
            }
            is Message.FetchDebugSettingsFailure -> {
                if (state is State.Loading) {
                    State.Error to emptySet()
                } else {
                    null
                }
            }
            is Message.SelectEndpointConfig -> {
                if (state is State.Content) {
                    state.copy(selectedEndpointConfigType = message.endpointConfigType) to emptySet()
                } else {
                    null
                }
            }
            is Message.ApplySettings -> {
                if (state is State.Content) {
                    state to setOf(Action.UpdateEndpointConfig(state.selectedEndpointConfigType))
                } else {
                    null
                }
            }
            is Message.ApplySettingsSuccess -> {
                if (state is State.Content) {
                    state to setOf(Action.ViewAction.RestartApplication)
                } else {
                    null
                }
            }
            is Message.ApplySettingsFailure -> {
                if (state is State.Content) {
                    state to emptySet()
                } else {
                    null
                }
            }
        } ?: (state to emptySet())
}