package org.hyperskill.app.track_selection.list.presentation

import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionListSelectConfirmationModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionListSelectConfirmationModalShownHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionListSelectConfirmationResultHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionListTrackClickedHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionListViewedHyperskillAnalyticEvent
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
            TrackSelectionListFeature.TrackSelectionResult.Success ->
                state to setOf(
                    Action.ViewAction.NavigateTo.StudyPlan,
                    Action.ViewAction.ShowTrackSelectionStatus.Success,
                )
            TrackSelectionListFeature.TrackSelectionResult.Error ->
                state to setOf(Action.ViewAction.ShowTrackSelectionStatus.Error)
            is Message.TrackClicked -> {
                val track = getTrackById(message.trackId, state)
                if (track != null) {
                    state to setOf(
                        Action.ViewAction.ShowTrackSelectionConfirmationModal(track),
                        InternalAction.LogAnalyticEvent(TrackSelectionListTrackClickedHyperskillAnalyticEvent(track.id))
                    )
                } else {
                    state to setOf(Action.ViewAction.ShowTrackSelectionStatus.Error)
                }
            }
            is Message.TrackSelectionConfirmationResult -> {
                if (state is State.Content) {
                    state to buildSet {
                        if (message.isConfirmed) {
                            add(Action.ViewAction.ShowTrackSelectionStatus.Loading)
                            add(InternalAction.SelectTrack(message.trackId))
                        }
                        add(
                            InternalAction.LogAnalyticEvent(
                                TrackSelectionListSelectConfirmationResultHyperskillAnalyticEvent(
                                    trackId = message.trackId,
                                    isConfirmed = message.isConfirmed
                                )
                            )
                        )
                    }
                } else {
                    state to emptySet()
                }
            }
            is Message.ViewedEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        TrackSelectionListViewedHyperskillAnalyticEvent()
                    )
                )
            is Message.TrackSelectionConfirmationModalShown ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        TrackSelectionListSelectConfirmationModalShownHyperskillAnalyticEvent()
                    )
                )
            is Message.TrackSelectionConfirmationModalHidden ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        TrackSelectionListSelectConfirmationModalHiddenHyperskillAnalyticEvent()
                    )
                )
        } ?: (state to emptySet())

    private fun getTrackById(trackId: Long, state: State): Track? =
        (state as? State.Content)?.tracks?.firstOrNull { it.track.id == trackId }?.track
}