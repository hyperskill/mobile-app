package org.hyperskill.app.track_list.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track_list.domain.analytic.TrackListClickedHyperskillAnalyticEvent
import org.hyperskill.app.track_list.domain.analytic.TrackListTrackModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.track_list.domain.analytic.TrackListTrackModalShownHyperskillAnalyticEvent
import org.hyperskill.app.track_list.domain.analytic.TrackListViewedHyperskillAnalyticEvent
import org.hyperskill.app.track_list.presentation.TrackListFeature.Action
import org.hyperskill.app.track_list.presentation.TrackListFeature.Message
import org.hyperskill.app.track_list.presentation.TrackListFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class TrackListReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.Initialize)
                } else {
                    null
                }
            is Message.TracksLoaded.Success ->
                if (state is State.Loading) {
                    State.Content(message.tracks, message.selectedTrackId) to emptySet()
                } else {
                    null
                }
            is Message.TracksLoaded.Error ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.StartLearningButtonClicked ->
                getTrackById(message.trackId, state)?.let { track ->
                    state to setOf(
                        Action.SelectTrack(track),
                        Action.LogAnalyticEvent(
                            TrackListClickedHyperskillAnalyticEvent(
                                HyperskillAnalyticPart.TRACK_MODAL,
                                HyperskillAnalyticTarget.START_LEARNING,
                                track.id
                            )
                        ),
                        Action.ViewAction.ShowTrackSelectionStatus.Loading
                    )
                }
            is Message.TrackSelected.Success ->
                state to setOf(
                    Action.ViewAction.NavigateTo.HomeScreen,
                    Action.ViewAction.ShowTrackSelectionStatus.Success,
                )
            is Message.TrackSelected.Error ->
                state to setOf(Action.ViewAction.ShowTrackSelectionStatus.Error)
            is Message.ProjectSelectionRequired ->
                state to setOf(Action.ViewAction.NavigateTo.ProjectSelection(message.trackId))
            is Message.TrackClicked ->
                getTrackById(message.trackId, state)?.let { track ->
                    state to setOf(
                        Action.ViewAction.ShowTrackModal(track),
                        Action.LogAnalyticEvent(
                            TrackListClickedHyperskillAnalyticEvent(
                                HyperskillAnalyticPart.MAIN,
                                HyperskillAnalyticTarget.TRACK,
                                message.trackId
                            )
                        )
                    )
                }
            is Message.ViewedEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        TrackListViewedHyperskillAnalyticEvent()
                    )
                )
            is Message.TrackModalShownEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        TrackListTrackModalShownHyperskillAnalyticEvent(
                            message.trackId
                        )
                    )
                )
            is Message.TrackModalHiddenEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        TrackListTrackModalHiddenHyperskillAnalyticEvent(
                            message.trackId
                        )
                    )
                )
        } ?: (state to emptySet())

    private fun getTrackById(trackId: Long, state: State): Track? =
        (state as? State.Content)?.tracksWithProgresses?.firstOrNull { it.track.id == trackId }?.track
}
