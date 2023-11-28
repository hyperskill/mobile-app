package org.hyperskill.app.leaderboards.screen.view.mapper

import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature
import org.hyperskill.app.leaderboards.widget.view.mapper.LeaderboardWidgetViewStateMapper

internal class LeaderboardScreenViewStateMapper(
    private val leaderboardWidgetViewStateMapper: LeaderboardWidgetViewStateMapper
) {
    fun map(state: LeaderboardScreenFeature.State): LeaderboardScreenFeature.ViewState =
        LeaderboardScreenFeature.ViewState(
            currentTab = state.currentTab,
            leaderboardWidgetViewState = leaderboardWidgetViewStateMapper.map(state.leaderboardWidgetState),
            toolbarState = state.toolbarState,
            isRefreshing = state.isRefreshing
        )
}