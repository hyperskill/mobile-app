package org.hyperskill.app.leaderboard.widget.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.leaderboard.domain.model.LeaderboardItem
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature
import org.hyperskill.app.leaderboard.widget.view.model.LeaderboardWidgetListItem

class LeaderboardWidgetViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: LeaderboardWidgetFeature.State): LeaderboardWidgetFeature.ViewState =
        when (state) {
            LeaderboardWidgetFeature.State.Idle -> LeaderboardWidgetFeature.ViewState.Idle
            LeaderboardWidgetFeature.State.Loading -> LeaderboardWidgetFeature.ViewState.Loading
            LeaderboardWidgetFeature.State.Error -> LeaderboardWidgetFeature.ViewState.Error
            is LeaderboardWidgetFeature.State.Content -> mapContentState(state)
        }

    private fun mapContentState(
        state: LeaderboardWidgetFeature.State.Content
    ): LeaderboardWidgetFeature.ViewState.Content =
        LeaderboardWidgetFeature.ViewState.Content(
            dailyLeaderboard = mapLeaderboardList(
                list = state.dailyLeaderboard,
                currentUserId = state.currentUserId
            ),
            weeklyLeaderboard = mapLeaderboardList(
                list = state.weeklyLeaderboard,
                currentUserId = state.currentUserId
            ),
            isRefreshing = state.isRefreshing
        )

    internal fun mapLeaderboardList(
        list: List<LeaderboardItem>,
        currentUserId: Long
    ): List<LeaderboardWidgetListItem> {
        val result = mutableListOf<LeaderboardWidgetListItem>()

        for (index in list.indices) {
            val previousLeaderboardItem = list.getOrNull(index - 1)
            val currentLeaderboardItem = list[index]

            if (previousLeaderboardItem != null &&
                previousLeaderboardItem.position != currentLeaderboardItem.position - 1
            ) {
                result.add(LeaderboardWidgetListItem.Separator)
            }

            val listItem = LeaderboardWidgetListItem.UserInfo(
                position = currentLeaderboardItem.position,
                passedProblems = currentLeaderboardItem.passedProblems,
                passedProblemsSubtitle = resourceProvider.getQuantityString(
                    SharedResources.plurals.problems_without_count,
                    currentLeaderboardItem.passedProblems,
                    currentLeaderboardItem.passedProblems
                ),
                userId = currentLeaderboardItem.user.id,
                userAvatar = currentLeaderboardItem.user.avatar,
                username = currentLeaderboardItem.user.fullname,
                isCurrentUser = currentLeaderboardItem.user.id == currentUserId
            )
            result.add(listItem)
        }

        return result
    }
}