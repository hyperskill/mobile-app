package org.hyperskill.app.leaderboards.widget.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature.Action
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class LeaderboardWidgetActionDispatcher(
    config: ActionDispatcherOptions
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        TODO("Not yet implemented")
    }
}