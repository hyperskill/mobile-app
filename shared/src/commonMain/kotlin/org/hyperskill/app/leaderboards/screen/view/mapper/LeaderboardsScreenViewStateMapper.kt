package org.hyperskill.app.leaderboards.screen.view.mapper

import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature
import org.hyperskill.app.leaderboards.screen.view.model.LeaderboardsScreenViewState

internal class LeaderboardsScreenViewStateMapper {
    fun map(state: LeaderboardsScreenFeature.State): LeaderboardsScreenViewState =
        when (state) {
            LeaderboardsScreenFeature.State.Idle -> LeaderboardsScreenViewState.Idle
            LeaderboardsScreenFeature.State.Loading -> LeaderboardsScreenViewState.Loading
            LeaderboardsScreenFeature.State.Error -> LeaderboardsScreenViewState.Error
            is LeaderboardsScreenFeature.State.Content -> getLoadedScreenContent(state)
        }

    private fun getLoadedScreenContent(
        state: LeaderboardsScreenFeature.State.Content
    ): LeaderboardsScreenViewState =
        // TODO: Handle empty
        LeaderboardsScreenViewState.Content(
            isRefreshing = state.isRefreshing
        )
}