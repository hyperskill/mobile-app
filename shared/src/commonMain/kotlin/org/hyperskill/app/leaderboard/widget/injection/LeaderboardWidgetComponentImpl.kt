package org.hyperskill.app.leaderboard.widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetActionDispatcher
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetReducer
import org.hyperskill.app.leaderboard.widget.presentation.MainLeaderboardWidgetActionDispatcher
import org.hyperskill.app.leaderboard.widget.view.mapper.LeaderboardWidgetViewStateMapper

internal class LeaderboardWidgetComponentImpl(
    private val appGraph: AppGraph
) : LeaderboardWidgetComponent {
    override val leaderboardWidgetReducer: LeaderboardWidgetReducer
        get() = LeaderboardWidgetReducer()

    override val leaderboardWidgetActionDispatcher: LeaderboardWidgetActionDispatcher
        get() = LeaderboardWidgetActionDispatcher(
            MainLeaderboardWidgetActionDispatcher(
                config = ActionDispatcherOptions(),
                leaderboardRepository = appGraph.buildLeaderboardDataComponent().leaderboardRepository,
                currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
                sentryInteractor = appGraph.sentryComponent.sentryInteractor
            ),
            appGraph.analyticComponent.analyticInteractor
        )

    override val leaderboardWidgetViewStateMapper: LeaderboardWidgetViewStateMapper
        get() = LeaderboardWidgetViewStateMapper(
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}