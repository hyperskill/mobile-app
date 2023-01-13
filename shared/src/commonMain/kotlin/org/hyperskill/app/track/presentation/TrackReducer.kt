package org.hyperskill.app.track.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsFeature
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsReducer
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
    private val navigationBarItemsReducer: NavigationBarItemsReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state.trackState is TrackState.Idle ||
                    (message.forceUpdate && (state.trackState is TrackState.Content || state.trackState is TrackState.NetworkError))
                ) {
                    state.copy(trackState = TrackState.Loading) to setOf(Action.FetchTrack)
                } else {
                    null
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
            is Message.PullToRefresh ->
                if (state.trackState is TrackState.Content && !state.trackState.isRefreshing) {
                    state.copy(trackState = state.trackState.copy(isRefreshing = true)) to setOf(
                        Action.FetchTrack,
                        Action.LogAnalyticEvent(TrackClickedPullToRefreshHyperskillAnalyticEvent())
                    )
                } else {
                    null
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
            is Message.NavigationBarItemsMessage -> {
                val (navigationBarItemsState, navigationBarItemsActions) = navigationBarItemsReducer.reduce(
                    state.navigationBarItemsState,
                    message.message
                )

                val actions = navigationBarItemsActions
                    .map {
                        if (it is NavigationBarItemsFeature.Action.ViewAction) {
                            Action.ViewAction.NavigationBarItemsViewAction(it)
                        } else {
                            Action.NavigationBarItemsAction(it)
                        }
                    }
                    .toSet()

                state.copy(navigationBarItemsState = navigationBarItemsState) to actions
            }
        } ?: (state to emptySet())
}