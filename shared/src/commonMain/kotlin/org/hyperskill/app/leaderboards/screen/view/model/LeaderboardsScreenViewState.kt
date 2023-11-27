package org.hyperskill.app.leaderboards.screen.view.model

sealed interface LeaderboardsScreenViewState {
    object Idle : LeaderboardsScreenViewState
    object Loading : LeaderboardsScreenViewState
    object Error : LeaderboardsScreenViewState
    object Empty : LeaderboardsScreenViewState
    data class Content(
        val isRefreshing: Boolean
    ) : LeaderboardsScreenViewState
}