package org.hyperskill.app.leaderboard.screen.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.InternalAction
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class LeaderboardScreenActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }
}