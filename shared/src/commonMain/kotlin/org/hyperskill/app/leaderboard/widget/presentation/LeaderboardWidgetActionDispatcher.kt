package org.hyperskill.app.leaderboard.widget.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.Action
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class LeaderboardWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            LeaderboardWidgetFeature.InternalAction.FetchLeaderboardData -> TODO()
            is LeaderboardWidgetFeature.InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
        }
    }
}