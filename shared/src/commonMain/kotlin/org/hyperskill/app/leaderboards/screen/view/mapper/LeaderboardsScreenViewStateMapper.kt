package org.hyperskill.app.leaderboards.screen.view.mapper

import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.LeaderboardState
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.LeaderboardViewState

internal class LeaderboardsScreenViewStateMapper {
    fun map(state: LeaderboardsScreenFeature.State): LeaderboardsScreenFeature.ViewState =
        LeaderboardsScreenFeature.ViewState(
            leaderboardViewState = mapLeaderboardState(state.leaderboardState),
            toolbarState = state.toolbarState,
            isRefreshing = state.isRefreshing
        )

    private fun mapLeaderboardState(state: LeaderboardState): LeaderboardViewState =
        when (state) {
            LeaderboardState.Idle -> LeaderboardViewState.Idle
            LeaderboardState.Loading -> LeaderboardViewState.Loading
            LeaderboardState.Error -> LeaderboardViewState.Error
            is LeaderboardState.Content -> LeaderboardViewState.Content
        }
}