package org.hyperskill.app.leaderboards.widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetActionDispatcher
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetReducer
import org.hyperskill.app.leaderboards.widget.view.mapper.LeaderboardWidgetViewStateMapper

internal class LeaderboardWidgetComponentImpl(
    private val appGraph: AppGraph
) : LeaderboardWidgetComponent {
    override val leaderboardWidgetReducer: LeaderboardWidgetReducer
        get() = LeaderboardWidgetReducer()

    override val leaderboardWidgetActionDispatcher: LeaderboardWidgetActionDispatcher
        get() = LeaderboardWidgetActionDispatcher(
            config = ActionDispatcherOptions()
        )

    override val leaderboardWidgetViewStateMapper: LeaderboardWidgetViewStateMapper
        get() = LeaderboardWidgetViewStateMapper()
}