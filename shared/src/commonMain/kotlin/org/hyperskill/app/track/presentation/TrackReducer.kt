package org.hyperskill.app.track.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.track.domain.analytic.TrackClickedContinueInWebHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackClickedGemsBarButtonItemHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackClickedStreakBarButtonItemHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackClickedTopicToDiscoverNextHyperskillAnalyticEvent
import org.hyperskill.app.track.domain.analytic.TrackViewedHyperskillAnalyticEvent
import org.hyperskill.app.track.presentation.TrackFeature.Action
import org.hyperskill.app.track.presentation.TrackFeature.Message
import org.hyperskill.app.track.presentation.TrackFeature.NavigationBarItemsState
import org.hyperskill.app.track.presentation.TrackFeature.State
import org.hyperskill.app.track.presentation.TrackFeature.State.WithNavigationBarItemsState
import ru.nobird.app.presentation.redux.reducer.StateReducer

class TrackReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading(NavigationBarItemsState.Loading) to setOf(
                        Action.FetchTrack,
                        Action.FetchNavigationBarItemsData
                    )
                } else {
                    null
                }
            is Message.TrackSuccess ->
                if (state is State.Loading) {
                    State.Content(
                        message.track,
                        message.trackProgress,
                        message.studyPlan,
                        message.topicsToDiscoverNext,
                        state.navigationBarItemsState
                    ) to emptySet()
                } else {
                    null
                }
            is Message.TrackFailure ->
                if (state is State.Loading) {
                    State.NetworkError(state.navigationBarItemsState) to emptySet()
                } else {
                    null
                }
            is Message.PullToRefresh ->
                if (state is State.Content && !state.isRefreshing) {
                    state.copy(
                        navigationBarItemsState = NavigationBarItemsState.Loading,
                        isRefreshing = true
                    ) to buildSet {
                        add(Action.FetchTrack)
                        if (state.navigationBarItemsState != NavigationBarItemsState.Loading) {
                            add(Action.FetchNavigationBarItemsData)
                        }
                        add(Action.LogAnalyticEvent(TrackClickedPullToRefreshHyperskillAnalyticEvent()))
                    }
                } else {
                    null
                }
            is Message.FetchNavigationBarItemsDataError ->
                when (state) {
                    is State.Loading ->
                        state.copy(navigationBarItemsState = NavigationBarItemsState.Error) to emptySet()
                    is State.NetworkError ->
                        state.copy(navigationBarItemsState = NavigationBarItemsState.Error) to emptySet()
                    is State.Content ->
                        state.copy(navigationBarItemsState = NavigationBarItemsState.Error) to emptySet()
                    is State.Idle ->
                        null
                }
            is Message.FetchNavigationBarItemsDataSuccess -> {
                val navigationBarItemsState = NavigationBarItemsState.Data(message.streak, message.hypercoinsBalance)
                when (state) {
                    is State.Loading ->
                        state.copy(navigationBarItemsState = navigationBarItemsState) to emptySet()
                    is State.NetworkError ->
                        state.copy(navigationBarItemsState = navigationBarItemsState) to emptySet()
                    is State.Content ->
                        state.copy(navigationBarItemsState = navigationBarItemsState) to emptySet()
                    is State.Idle ->
                        null
                }
            }
            // Flow Messages
            is Message.StepQuizSolved -> {
                if (state is WithNavigationBarItemsState && state.navigationBarItemsState is NavigationBarItemsState.Data) {
                    val castedNavigationBarItemsState = state.navigationBarItemsState as NavigationBarItemsState.Data
                    val newNavigationBarItemsState = castedNavigationBarItemsState.copy(
                        streak = castedNavigationBarItemsState.streak?.getStreakWithTodaySolved()
                    )

                    when (state) {
                        is State.Loading ->
                            state.copy(navigationBarItemsState = newNavigationBarItemsState) to emptySet()
                        is State.NetworkError ->
                            state.copy(navigationBarItemsState = newNavigationBarItemsState) to emptySet()
                        is State.Content ->
                            state.copy(navigationBarItemsState = newNavigationBarItemsState) to emptySet()
                        is State.Idle ->
                            null
                    }
                } else {
                    null
                }
            }
            is Message.HypercoinsBalanceChanged -> {
                if (state is WithNavigationBarItemsState) {
                    val newNavigationBarItemsState = if (state.navigationBarItemsState is NavigationBarItemsState.Data) {
                        val castedNavigationBarItemsState = state.navigationBarItemsState as NavigationBarItemsState.Data
                        castedNavigationBarItemsState.copy(hypercoinsBalance = message.hypercoinsBalance)
                    } else {
                        NavigationBarItemsState.Data(null, message.hypercoinsBalance)
                    }

                    when (state) {
                        is State.Loading ->
                            state.copy(navigationBarItemsState = newNavigationBarItemsState) to emptySet()
                        is State.NetworkError ->
                            state.copy(navigationBarItemsState = newNavigationBarItemsState) to emptySet()
                        is State.Content ->
                            state.copy(navigationBarItemsState = newNavigationBarItemsState) to emptySet()
                        is State.Idle ->
                            null
                    }
                } else {
                    null
                }
            }
            // Click Messages
            is Message.TopicToDiscoverNextClicked -> {
                val targetTheoryId = (state as? State.Content)
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
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = true) to setOf(
                        Action.GetMagicLink(HyperskillUrlPath.StudyPlan()),
                        Action.LogAnalyticEvent(TrackClickedContinueInWebHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            Message.ClickedGemsBarButtonItem ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.ProfileTab,
                        Action.LogAnalyticEvent(TrackClickedGemsBarButtonItemHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            Message.ClickedStreakBarButtonItem ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.ProfileTab,
                        Action.LogAnalyticEvent(TrackClickedStreakBarButtonItemHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            // MagicLinks Messages
            is Message.GetMagicLinkReceiveSuccess ->
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.OpenUrl(message.url))
                } else {
                    null
                }
            is Message.GetMagicLinkReceiveFailure ->
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.ShowGetMagicLinkError)
                } else {
                    null
                }
            // Analytic Messages
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(TrackViewedHyperskillAnalyticEvent()))
        } ?: (state to emptySet())
}