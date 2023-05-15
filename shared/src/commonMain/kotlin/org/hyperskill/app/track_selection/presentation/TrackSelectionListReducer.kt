package org.hyperskill.app.track_selection.presentation

import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionListTrackClickedHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionListViewedHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class TrackSelectionListReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(InternalAction.Initialize)
                } else {
                    null
                }
            is TrackSelectionListFeature.TracksFetchResult.Success ->
                if (state is State.Loading) {
                    State.Content(message.tracks, message.selectedTrackId) to emptySet()
                } else {
                    null
                }
            TrackSelectionListFeature.TracksFetchResult.Error ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            TrackSelectionListFeature.TrackSelectionResult.Success ->
                state to setOf(
                    Action.ViewAction.NavigateTo.StudyPlan,
                    Action.ViewAction.ShowTrackSelectionStatus.Success,
                )
            TrackSelectionListFeature.TrackSelectionResult.Error ->
                state to setOf(Action.ViewAction.ShowTrackSelectionStatus.Error)
            is Message.TrackClicked ->
                getTrackById(message.trackId, state)?.let { track ->
                    state to setOf(
                        Action.ViewAction.ShowTrackSelectionStatus.Loading,
                        InternalAction.SelectTrack(track),
                        InternalAction.LogAnalyticEvent(TrackSelectionListTrackClickedHyperskillAnalyticEvent(track.id))
                    )
                }
            is Message.ViewedEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        TrackSelectionListViewedHyperskillAnalyticEvent()
                    )
                )
        } ?: (state to emptySet())

    private fun getTrackById(trackId: Long, state: State): Track? =
        (state as? State.Content)?.tracks?.firstOrNull { it.track.id == trackId }?.track
}