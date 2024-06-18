package org.hyperskill.app.leaderboard.widget.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.Action
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.Message

class LeaderboardWidgetActionDispatcher internal constructor(
    mainLeaderboardWidgetActionDispatcher: MainLeaderboardWidgetActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<Action, Message>(
    listOf(
        mainLeaderboardWidgetActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? LeaderboardWidgetFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
        }
    )
)