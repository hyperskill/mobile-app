package org.hyperskill.app.leaderboards.screen.view.mapper

import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.LeaderboardState
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.LeaderboardViewState

internal class LeaderboardScreenViewStateMapper {
    fun map(state: LeaderboardScreenFeature.State): LeaderboardScreenFeature.ViewState =
        LeaderboardScreenFeature.ViewState(
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