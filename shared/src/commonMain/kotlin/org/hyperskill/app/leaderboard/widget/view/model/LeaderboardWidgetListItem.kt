package org.hyperskill.app.leaderboard.widget.view.model

sealed interface LeaderboardWidgetListItem {
    data class UserInfo(
        val position: Int,
        val passedProblems: Int,
        val passedProblemsSubtitle: String,
        val userId: Long,
        val userAvatar: String,
        val userName: String,
        val isCurrentUser: Boolean
    ) : LeaderboardWidgetListItem

    object Separator : LeaderboardWidgetListItem
}