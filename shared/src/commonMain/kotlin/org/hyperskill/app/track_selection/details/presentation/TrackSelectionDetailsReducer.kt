package org.hyperskill.app.track_selection.details.presentation

import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class TrackSelectionDetailsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            else -> TODO()
        }
}