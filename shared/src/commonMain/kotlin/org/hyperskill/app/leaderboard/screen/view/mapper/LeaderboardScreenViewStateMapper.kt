package org.hyperskill.app.leaderboard.screen.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature
import org.hyperskill.app.leaderboard.widget.view.mapper.LeaderboardWidgetViewStateMapper

internal class LeaderboardScreenViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val dateFormatter: SharedDateFormatter,
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
            isRefreshing = state.isRefreshing,
            updatesInText = getUpdatesInText(state)
        )

    private fun mapLeaderboardWidgetState(
        state: LeaderboardWidgetFeature.State,
        currentTab: LeaderboardScreenFeature.Tab
    ): LeaderboardScreenFeature.ListViewState =
        when (val viewState = leaderboardWidgetViewStateMapper.map(state)) {
            LeaderboardWidgetFeature.ViewState.Idle -> LeaderboardScreenFeature.ListViewState.Idle
            LeaderboardWidgetFeature.ViewState.Loading -> LeaderboardScreenFeature.ListViewState.Loading
            LeaderboardWidgetFeature.ViewState.Error -> LeaderboardScreenFeature.ListViewState.Error
            is LeaderboardWidgetFeature.ViewState.Content -> {
                val list = when (currentTab) {
                    LeaderboardScreenFeature.Tab.DAY -> viewState.dailyLeaderboard
                    LeaderboardScreenFeature.Tab.WEEK -> viewState.weeklyLeaderboard
                }
                if (list.isEmpty()) {
                    LeaderboardScreenFeature.ListViewState.Empty
                } else {
                    LeaderboardScreenFeature.ListViewState.Content(list)
                }
            }
        }

    private fun getUpdatesInText(state: LeaderboardScreenFeature.State): String? {
        val formattedDate = when (state.currentTab) {
            LeaderboardScreenFeature.Tab.DAY ->
                state.dailyLeaderboardSecondsUntilNextUpdate?.let { seconds ->
                    dateFormatter.formatHoursWithMinutesCount(seconds)
                }
            LeaderboardScreenFeature.Tab.WEEK ->
                state.weeklyLeaderboardSecondsUntilNextUpdate?.let { seconds ->
                    dateFormatter.formatDaysWithHoursAndMinutesCount(seconds)
                }
        }
        return formattedDate?.let { resourceProvider.getString(SharedResources.strings.leaderboard_update_in_text, it) }
    }
}