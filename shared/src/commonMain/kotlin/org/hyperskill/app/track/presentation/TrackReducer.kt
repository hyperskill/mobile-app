package org.hyperskill.app.track.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.track.domain.analytic.TrackClickedContinueInWebHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackClickedTopicToDiscoverNextHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackViewedHyperskillAnalyticEvent
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import org.hyperskill.app.track.presentation.TrackFeature.State
import org.hyperskill.app.track.presentation.TrackFeature.TrackState
import ru.nobird.app.presentation.redux.reducer.StateReducer

class TrackReducer(
    private val gamificationToolbarReducer: GamificationToolbarReducer
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

                state.copy(trackState = trackState, toolbarState = toolbarState) to trackActions + toolbarActions
            }
            is Message.TrackSuccess ->
                state.copy(
                    trackState = TrackState.Content(
                        message.track,
                        message.trackProgress,
                        message.studyPlan,
                        message.topicsToDiscoverNext
                    )
                ) to emptySet()
            is Message.TrackFailure ->
                state.copy(trackState = TrackState.NetworkError) to emptySet()
            is Message.PullToRefresh -> {
                val (trackState, trackActions) = if (
                    state.trackState is TrackState.Content && !state.trackState.isRefreshing
                ) {
                    state.trackState.copy(isRefreshing = true) to setOf(
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

                state.copy(trackState = trackState, toolbarState = toolbarState) to trackActions + toolbarActions
            }
            // Click Messages
            is Message.TopicToDiscoverNextClicked -> {
                val targetTheoryId = (state.trackState as? TrackState.Content)
                    ?.topicsToDiscoverNext?.firstOrNull { it.id == message.topicId }
                    ?.theoryId

                if (targetTheoryId != null) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.StepScreen(targetTheoryId),
                        Action.LogAnalyticEvent(
                            TrackClickedTopicToDiscoverNextHyperskillAnalyticEvent(
                                topicId = message.topicId,
                                theoryId = targetTheoryId
                            )
                        )
                    )
                } else {
                    null
                }
            }
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
        } ?: (state to emptySet())

    /**
     * Reduces [Message.GamificationToolbarMessage] to [GamificationToolbarFeature.State] and set of [TrackFeature.Action]
     */
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
}