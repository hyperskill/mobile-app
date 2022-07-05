package org.hyperskill.app.track.presentation

import ru.nobird.app.presentation.redux.reducer.StateReducer
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import org.hyperskill.app.track.presentation.TrackFeature.State

class TrackReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Init ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.FetchTrack(message.trackId))
                } else {
                    null
                }
            is Message.TrackSuccess ->
                State.Content(message.track, message.trackProgress) to emptySet()
            is Message.TrackError ->
                State.NetworkError to emptySet()
        } ?: (state to emptySet())
}