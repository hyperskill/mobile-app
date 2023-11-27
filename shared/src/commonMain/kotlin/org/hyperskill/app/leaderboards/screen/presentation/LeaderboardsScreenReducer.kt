package org.hyperskill.app.leaderboards.screen.presentation

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.domain.analytic.LeaderboardViewedHyperskillAnalyticEvent
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.InternalAction
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.LeaderboardState
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias LeaderboardsScreenReducerResult = Pair<State, Set<Action>>

internal class LeaderboardsScreenReducer(
    private val gamificationToolbarReducer: GamificationToolbarReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): LeaderboardsScreenReducerResult =
        when (message) {
            Message.Initialize -> {
                initializeFeatures(state, retryContentLoadingClicked = false)
            }
            Message.PullToRefresh -> {
                // TODO: Handle pull to refresh
                state to setOf(InternalAction.LogAnalyticEvent(LeaderboardClickedPullToRefreshHyperskillAnalyticEvent))
            }
            Message.RetryContentLoading -> {
                initializeFeatures(state, retryContentLoadingClicked = true)
            }
            LeaderboardsScreenFeature.InternalMessage.FetchLeaderboardsError -> TODO()
            LeaderboardsScreenFeature.InternalMessage.FetchLeaderboardsSuccess -> TODO()
            Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(LeaderboardViewedHyperskillAnalyticEvent))
            }
            // Wrapper Messages
            is Message.GamificationToolbarMessage -> {
                val (toolbarState, toolbarActions) =
                    reduceGamificationToolbarMessage(state.toolbarState, message.message)
                state.copy(toolbarState = toolbarState) to toolbarActions
            }
        }

    private fun initializeFeatures(
        state: State,
        retryContentLoadingClicked: Boolean
    ): LeaderboardsScreenReducerResult {
        val shouldReloadLeaderboard =
            state.leaderboardState is LeaderboardState.Idle ||
                retryContentLoadingClicked &&
                (state.leaderboardState is LeaderboardState.Content || state.leaderboardState is LeaderboardState.Error)
        val (leaderboardState, leaderboardActions) =
            if (shouldReloadLeaderboard) {
                LeaderboardState.Loading to setOf(InternalAction.FetchLeaderboards)
            } else {
                state.leaderboardState to emptySet()
            }

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
            leaderboardState = leaderboardState,
            toolbarState = toolbarState
        ) to (leaderboardActions + toolbarActions + analyticActions)
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