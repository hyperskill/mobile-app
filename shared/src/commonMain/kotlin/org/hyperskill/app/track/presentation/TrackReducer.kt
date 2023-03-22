package org.hyperskill.app.track.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextReducer
import org.hyperskill.app.track.domain.analytic.TrackClickedContinueInWebHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackViewedHyperskillAnalyticEvent
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import org.hyperskill.app.track.presentation.TrackFeature.State
import org.hyperskill.app.track.presentation.TrackFeature.TrackState
import ru.nobird.app.presentation.redux.reducer.StateReducer

class TrackReducer(
    private val gamificationToolbarReducer: GamificationToolbarReducer,
    private val topicsToDiscoverNextReducer: TopicsToDiscoverNextReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                val (trackState, trackActions) = if (state.trackState is TrackState.Idle ||
                    (message.forceUpdate && (state.trackState is TrackState.Content || state.trackState is TrackState.NetworkError))
                ) {
                    TrackState.Loading to setOf(Action.FetchTrack)
                } else {
                    state.trackState to emptySet()
                }

                val (toolbarState, toolbarActions) = reduceGamificationToolbarMessage(
                    state.toolbarState,
                    GamificationToolbarFeature.Message.Initialize(GamificationToolbarScreen.TRACK, message.forceUpdate)
                )

                val (topicsToDiscoverNextState, topicsToDiscoverNextActions) = reduceTopicsToDiscoverNextMessage(
                    state.topicsToDiscoverNextState,
                    TopicsToDiscoverNextFeature.Message.Initialize(message.forceUpdate)
                )

                state.copy(
                    trackState = trackState,
                    toolbarState = toolbarState,
                    topicsToDiscoverNextState = topicsToDiscoverNextState
                ) to trackActions + toolbarActions + topicsToDiscoverNextActions
            }
            is Message.TrackSuccess ->
                state.copy(
                    trackState = TrackState.Content(
                        message.track,
                        message.trackProgress,
                        message.studyPlan
                    )
                ) to emptySet()
            is Message.TrackFailure ->
                state.copy(trackState = TrackState.NetworkError) to emptySet()
            is Message.PullToRefresh -> {
                val (trackState, trackActions) = if (
                    state.trackState is TrackState.Content && !state.trackState.isRefreshing
                ) {
                    state.trackState.copy(isRefreshing = true) to setOf(
                        Action.ResetStateRepositories,
                        Action.FetchTrack,
                        Action.LogAnalyticEvent(TrackClickedPullToRefreshHyperskillAnalyticEvent())
                    )
                } else {
                    state.trackState to emptySet()
                }

                val (toolbarState, toolbarActions) = reduceGamificationToolbarMessage(
                    state.toolbarState,
                    GamificationToolbarFeature.Message.PullToRefresh(GamificationToolbarScreen.TRACK)
                )

                val (topicsToDiscoverNextState, topicsToDiscoverNextActions) = reduceTopicsToDiscoverNextMessage(
                    state.topicsToDiscoverNextState,
                    TopicsToDiscoverNextFeature.Message.PullToRefresh
                )

                state.copy(
                    trackState = trackState,
                    toolbarState = toolbarState,
                    topicsToDiscoverNextState = topicsToDiscoverNextState
                ) to trackActions + toolbarActions + topicsToDiscoverNextActions
            }
            // Click Messages
            is Message.ClickedContinueInWeb ->
                if (state.trackState is TrackState.Content) {
                    state.copy(trackState = state.trackState.copy(isLoadingMagicLink = true)) to setOf(
                        Action.GetMagicLink(HyperskillUrlPath.StudyPlan()),
                        Action.LogAnalyticEvent(TrackClickedContinueInWebHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            // MagicLinks Messages
            is Message.GetMagicLinkReceiveSuccess ->
                if (state.trackState is TrackState.Content) {
                    state.copy(trackState = state.trackState.copy(isLoadingMagicLink = false)) to setOf(
                        Action.ViewAction.OpenUrl(message.url)
                    )
                } else {
                    null
                }
            is Message.GetMagicLinkReceiveFailure ->
                if (state.trackState is TrackState.Content) {
                    state.copy(trackState = state.trackState.copy(isLoadingMagicLink = false)) to setOf(
                        Action.ViewAction.ShowGetMagicLinkError
                    )
                } else {
                    null
                }
            // Analytic Messages
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(TrackViewedHyperskillAnalyticEvent()))
            // Wrapper Messages
            is Message.GamificationToolbarMessage -> {
                val (toolbarState, toolbarActions) =
                    reduceGamificationToolbarMessage(state.toolbarState, message.message)
                state.copy(toolbarState = toolbarState) to toolbarActions
            }
            is Message.TopicsToDiscoverNextMessage -> {
                val (topicsToDiscoverNextState, topicsToDiscoverNextActions) =
                    reduceTopicsToDiscoverNextMessage(state.topicsToDiscoverNextState, message.message)
                state.copy(topicsToDiscoverNextState = topicsToDiscoverNextState) to topicsToDiscoverNextActions
            }
        } ?: (state to emptySet())

    private fun reduceGamificationToolbarMessage(
        state: GamificationToolbarFeature.State,
        message: GamificationToolbarFeature.Message
    ): Pair<GamificationToolbarFeature.State, Set<Action>> {
        val (gamificationToolbarState, gamificationToolbarActions) = gamificationToolbarReducer.reduce(state, message)

        val actions = gamificationToolbarActions
            .map {
                if (it is GamificationToolbarFeature.Action.ViewAction) {
                    Action.ViewAction.GamificationToolbarViewAction(it)
                } else {
                    Action.GamificationToolbarAction(it)
                }
            }
            .toSet()

        return gamificationToolbarState to actions
    }

    private fun reduceTopicsToDiscoverNextMessage(
        state: TopicsToDiscoverNextFeature.State,
        message: TopicsToDiscoverNextFeature.Message
    ): Pair<TopicsToDiscoverNextFeature.State, Set<Action>> {
        val (topicsToDiscoverNextState, topicsToDiscoverNextActions) =
            topicsToDiscoverNextReducer.reduce(state, message)

        val actions = topicsToDiscoverNextActions
            .map {
                if (it is TopicsToDiscoverNextFeature.Action.ViewAction) {
                    Action.ViewAction.TopicsToDiscoverNextViewAction(it)
                } else {
                    Action.TopicsToDiscoverNextAction(it)
                }
            }
            .toSet()

        return topicsToDiscoverNextState to actions
    }
}