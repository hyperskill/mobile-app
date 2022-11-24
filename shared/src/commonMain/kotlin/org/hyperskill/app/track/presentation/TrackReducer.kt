package org.hyperskill.app.track.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.track.domain.analytic.TrackClickedContinueInWebHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackViewedHyperskillAnalyticEvent
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import org.hyperskill.app.track.presentation.TrackFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class TrackReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.FetchTrack)
                } else {
                    null
                }
            is Message.TrackSuccess ->
                State.Content(message.track, message.trackProgress, message.studyPlan) to emptySet()
            is Message.TrackFailure ->
                State.NetworkError to emptySet()
            is Message.PullToRefresh ->
                if (state is State.Content && !state.isRefreshing) {
                    state.copy(isRefreshing = true) to setOf(
                        Action.FetchTrack,
                        Action.LogAnalyticEvent(TrackClickedPullToRefreshHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(TrackViewedHyperskillAnalyticEvent()))
            Message.ClickedContinueInWeb ->
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = true) to setOf(
                        Action.GetMagicLink(HyperskillUrlPath.StudyPlan()),
                        Action.LogAnalyticEvent(TrackClickedContinueInWebHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            is Message.GetMagicLinkReceiveSuccess ->
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.OpenUrl(message.url))
                } else {
                    null
                }
            Message.GetMagicLinkReceiveFailure ->
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.ShowGetMagicLinkError)
                } else {
                    null
                }
        } ?: (state to emptySet())
}