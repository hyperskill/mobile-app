package org.hyperskill.app.leaderboards.widget.injection

import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetActionDispatcher
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetReducer
import org.hyperskill.app.leaderboards.widget.view.mapper.LeaderboardWidgetViewStateMapper

interface LeaderboardWidgetComponent {
    val leaderboardWidgetReducer: LeaderboardWidgetReducer
    val leaderboardWidgetActionDispatcher: LeaderboardWidgetActionDispatcher
    val leaderboardWidgetViewStateMapper: LeaderboardWidgetViewStateMapper
}