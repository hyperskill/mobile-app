package org.hyperskill.app.leaderboard.widget.injection

import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetActionDispatcher
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetReducer
import org.hyperskill.app.leaderboard.widget.view.mapper.LeaderboardWidgetViewStateMapper

interface LeaderboardWidgetComponent {
    val leaderboardWidgetReducer: LeaderboardWidgetReducer
    val leaderboardWidgetActionDispatcher: LeaderboardWidgetActionDispatcher
    val leaderboardWidgetViewStateMapper: LeaderboardWidgetViewStateMapper
}