package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.placeholder_new_user.domain.analytic.PlaceholderNewUserClickedHyperskillAnalyticEvent
import org.hyperskill.app.placeholder_new_user.domain.analytic.PlaceholderNewUserTrackModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.placeholder_new_user.domain.analytic.PlaceholderNewUserTrackModalShownHyperskillAnalyticEvent
import org.hyperskill.app.placeholder_new_user.domain.analytic.PlaceholderNewUserViewedHyperskillAnalyticEvent
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Action
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Message
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.State
import org.hyperskill.app.track.domain.model.Track
import ru.nobird.app.presentation.redux.reducer.StateReducer

class PlaceholderNewUserReducer : StateReducer<State, Message, Action> {
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
                    State.Content(message.tracks) to emptySet()
                } else {
                    null
                }
            is Message.TracksLoaded.Error ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.StartLearningButtonClicked -> {
                getTrackById(message.trackId, state)?.let { track ->
                    state to setOf(
                        Action.SelectTrack(track),
                        Action.LogAnalyticEvent(
                            PlaceholderNewUserClickedHyperskillAnalyticEvent(
                                HyperskillAnalyticPart.TRACK_MODAL,
                                HyperskillAnalyticTarget.START_LEARNING,
                                track.id
                            )
                        ),
                        Action.ViewAction.ShowTrackSelectionStatus.Loading
                    )
                }
            }
            is Message.TrackSelected.Success -> {
                state to setOf(
                    Action.ViewAction.NavigateTo.HomeScreen,
                    Action.ViewAction.ShowTrackSelectionStatus.Success,
                )
            }
            is Message.TrackSelected.Error -> {
                state to setOf(Action.ViewAction.ShowTrackSelectionStatus.Error)
            }
            is Message.ViewedEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        PlaceholderNewUserViewedHyperskillAnalyticEvent()
                    )
                )
            is Message.TrackTapped -> {
                getTrackById(message.trackId, state)?.let { track ->
                    state to setOf(
                        Action.ViewAction.ShowTrackModal(track),
                        Action.LogAnalyticEvent(
                            PlaceholderNewUserClickedHyperskillAnalyticEvent(
                                HyperskillAnalyticPart.MAIN,
                                HyperskillAnalyticTarget.TRACK,
                                message.trackId
                            )
                        )
                    )
                }
            }
            is Message.TrackModalShownEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        PlaceholderNewUserTrackModalShownHyperskillAnalyticEvent(
                            message.trackId
                        )
                    )
                )
            is Message.TrackModalHiddenEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        PlaceholderNewUserTrackModalHiddenHyperskillAnalyticEvent(
                            message.trackId
                        )
                    )
                )
        } ?: (state to emptySet())
}

private fun getTrackById(trackId: Long, state: State): Track? =
    (state as? State.Content)?.tracks?.firstOrNull { it.id == trackId }
