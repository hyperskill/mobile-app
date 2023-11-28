package org.hyperskill.app.leaderboards.widget.view.model

sealed interface LeaderboardWidgetListItem {
    data class UserInfo(
        val position: Int,
        val passedProblems: Int,
        val passedProblemsSubtitle: String,
        val userAvatar: String,
        val username: String,
        val isCurrentUser: Boolean
    ) : LeaderboardWidgetListItem

    object Separator : LeaderboardWidgetListItem
}