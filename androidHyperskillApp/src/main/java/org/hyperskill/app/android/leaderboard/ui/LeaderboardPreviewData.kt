package org.hyperskill.app.android.leaderboard.ui

import org.hyperskill.app.leaderboard.widget.view.model.LeaderboardWidgetListItem

object LeaderboardPreviewData {
    val listData: List<LeaderboardWidgetListItem> =
        buildList {
            repeat(6) { index ->
                add(
                    LeaderboardWidgetListItem.UserInfo(
                        position = index + 1,
                        passedProblems = 100 - index,
                        passedProblemsSubtitle = "problems",
                        userId = index.toLong(),
                        userAvatar = null,
                        userName = "Test user name",
                        isCurrentUser = false
                    )
                )
                if (index == 2) {
                    add(LeaderboardWidgetListItem.Separator)
                }
            }
        }
}