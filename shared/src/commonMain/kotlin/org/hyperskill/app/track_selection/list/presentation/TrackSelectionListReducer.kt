package org.hyperskill.app.track_selection.list.presentation

import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.list.domain.analytic.TrackSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.track_selection.list.domain.analytic.TrackSelectionListTrackClickedHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.list.domain.analytic.TrackSelectionListViewedHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class TrackSelectionListReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle) {
                    State.Loading to setOf(InternalAction.FetchTracks)
                } else {
                    null
                }
            is Message.RetryContentLoading ->
                if (state is State.NetworkError) {
                    State.Loading to setOf(
                        InternalAction.FetchTracks,
                        InternalAction.LogAnalyticEvent(
                            TrackSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent()
                        )
                    )
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
            is Message.TrackClicked -> {
                val track = getTrackById(message.trackId, state)
                if (track != null) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.TrackDetails(
                            track,
                            isTrackSelected = state is State.Content && state.selectedTrackId == track.track.id
                        ),
                        InternalAction.LogAnalyticEvent(
                            TrackSelectionListTrackClickedHyperskillAnalyticEvent(
                                track.track.id
                            )
                        )
                    )
                } else {
                    state to setOf(Action.ViewAction.ShowTrackSelectionError)
                }
            }
            is Message.ViewedEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        TrackSelectionListViewedHyperskillAnalyticEvent()
                    )
                )
        } ?: (state to emptySet())

    private fun getTrackById(trackId: Long, state: State): TrackWithProgress? =
        (state as? State.Content)?.tracks?.firstOrNull { it.track.id == trackId }
}