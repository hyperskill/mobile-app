package org.hyperskill.app.leaderboards.screen.presentation

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardClickedTabHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardViewedHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.InternalAction
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.State
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias LeaderboardScreenReducerResult = Pair<State, Set<Action>>

internal class LeaderboardScreenReducer(
    private val leaderWidgetReducer: LeaderboardWidgetReducer,
    private val gamificationToolbarReducer: GamificationToolbarReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): LeaderboardScreenReducerResult =
        when (message) {
            Message.Initialize -> {
                initializeFeatures(state, retryContentLoadingClicked = false)
            }
            Message.RetryContentLoading -> {
                initializeFeatures(state, retryContentLoadingClicked = true)
            }
            Message.PullToRefresh -> {
                handlePullToRefreshMessage(state)
            }
            Message.ScreenBecomesActive -> {
                val (leaderboardWidgetState, leaderboardWidgetActions) =
                    reduceLeaderboardWidgetMessage(
                        state.leaderboardWidgetState,
                        LeaderboardWidgetFeature.InternalMessage.ReloadContentInBackground
                    )
                state.copy(leaderboardWidgetState = leaderboardWidgetState) to leaderboardWidgetActions
            }
            is Message.TabClicked -> {
                if (state.currentTab != message.tab) {
                    state.copy(currentTab = message.tab) to setOf(
                        InternalAction.LogAnalyticEvent(LeaderboardClickedTabHyperskillAnalyticEvent(message.tab))
                    )
                } else {
                    null
                }
            }
            Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(LeaderboardViewedHyperskillAnalyticEvent))
            }
            // Wrapper Messages
            is Message.LeaderboardWidgetMessage -> {
                val (leaderboardWidgetState, leaderboardWidgetActions) =
                    reduceLeaderboardWidgetMessage(state.leaderboardWidgetState, message.message)
                state.copy(leaderboardWidgetState = leaderboardWidgetState) to leaderboardWidgetActions
            }
            is Message.GamificationToolbarMessage -> {
                val (toolbarState, toolbarActions) =
                    reduceGamificationToolbarMessage(state.toolbarState, message.message)
                state.copy(toolbarState = toolbarState) to toolbarActions
            }
        } ?: (state to emptySet())

    private fun initializeFeatures(
        state: State,
        retryContentLoadingClicked: Boolean
    ): LeaderboardScreenReducerResult {
        val (leaderboardWidgetState, leaderboardWidgetActions) =
            reduceLeaderboardWidgetMessage(
                state.leaderboardWidgetState,
                LeaderboardWidgetFeature.InternalMessage.Initialize(forceUpdate = retryContentLoadingClicked)
            )

        val (toolbarState, toolbarActions) =
            reduceGamificationToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.InternalMessage.Initialize(retryContentLoadingClicked)
            )

        val analyticActions = if (retryContentLoadingClicked) {
            setOf(InternalAction.LogAnalyticEvent(LeaderboardClickedRetryContentLoadingHyperskillAnalyticEvent))
        } else {
            emptySet()
        }

        return state.copy(
            leaderboardWidgetState = leaderboardWidgetState,
            toolbarState = toolbarState
        ) to (leaderboardWidgetActions + toolbarActions + analyticActions)
    }

    private fun handlePullToRefreshMessage(state: State): LeaderboardScreenReducerResult {
        val (leaderboardWidgetState, leaderboardWidgetActions) =
            reduceLeaderboardWidgetMessage(
                state.leaderboardWidgetState,
                LeaderboardWidgetFeature.InternalMessage.PullToRefresh
            )

        val (toolbarState, toolbarActions) =
            reduceGamificationToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.InternalMessage.PullToRefresh
            )

        val analyticActions =
            setOf(InternalAction.LogAnalyticEvent(LeaderboardClickedPullToRefreshHyperskillAnalyticEvent))

        return state.copy(
            leaderboardWidgetState = leaderboardWidgetState,
            toolbarState = toolbarState
        ) to (leaderboardWidgetActions + toolbarActions + analyticActions)
    }

    private fun reduceLeaderboardWidgetMessage(
        state: LeaderboardWidgetFeature.State,
        message: LeaderboardWidgetFeature.Message
    ): Pair<LeaderboardWidgetFeature.State, Set<Action>> {
        val (leaderboardWidgetState, leaderboardWidgetActions) = leaderWidgetReducer.reduce(state, message)

        val actions = leaderboardWidgetActions
            .map {
                if (it is LeaderboardWidgetFeature.Action.ViewAction) {
                    Action.ViewAction.LeaderboardWidgetViewAction(it)
                } else {
                    InternalAction.LeaderboardWidgetAction(it)
                }
            }
            .toSet()

        return leaderboardWidgetState to actions
    }

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
                    InternalAction.GamificationToolbarAction(it)
                }
            }
            .toSet()

        return gamificationToolbarState to actions
    }
}