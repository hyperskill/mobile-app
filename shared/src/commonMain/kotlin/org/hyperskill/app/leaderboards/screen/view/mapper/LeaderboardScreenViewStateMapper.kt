package org.hyperskill.app.leaderboards.screen.view.mapper

import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature
import org.hyperskill.app.leaderboards.widget.view.mapper.LeaderboardWidgetViewStateMapper

internal class LeaderboardScreenViewStateMapper(
    private val leaderboardWidgetViewStateMapper: LeaderboardWidgetViewStateMapper
) {
    fun map(state: LeaderboardScreenFeature.State): LeaderboardScreenFeature.ViewState =
        LeaderboardScreenFeature.ViewState(
            currentTab = state.currentTab,
            listViewState = mapLeaderboardWidgetState(
                state = state.leaderboardWidgetState,
                currentTab = state.currentTab
            ),
            toolbarState = state.toolbarState,
            isRefreshing = state.isRefreshing
        )

    private fun mapLeaderboardWidgetState(
        state: LeaderboardWidgetFeature.State,
        currentTab: LeaderboardScreenFeature.Tab
    ): LeaderboardScreenFeature.ViewState.ListViewState =
        when (val viewState = leaderboardWidgetViewStateMapper.map(state)) {
            LeaderboardWidgetFeature.ViewState.Idle -> LeaderboardScreenFeature.ViewState.ListViewState.Idle
            LeaderboardWidgetFeature.ViewState.Loading -> LeaderboardScreenFeature.ViewState.ListViewState.Loading
            LeaderboardWidgetFeature.ViewState.Error -> LeaderboardScreenFeature.ViewState.ListViewState.Error
            is LeaderboardWidgetFeature.ViewState.Content -> {
                val list = when (currentTab) {
                    LeaderboardScreenFeature.Tab.DAY -> viewState.dailyLeaderboard
                    LeaderboardScreenFeature.Tab.WEEK -> viewState.weeklyLeaderboard
                }
                LeaderboardScreenFeature.ViewState.ListViewState.Content(list)
            }
        }
}