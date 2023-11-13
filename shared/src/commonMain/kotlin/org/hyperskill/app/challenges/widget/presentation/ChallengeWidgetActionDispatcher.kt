package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.domain.repository.ChallengesRepository
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Action
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Message
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ChallengeWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val challengesRepository: ChallengesRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        super.doSuspendableAction(action)
    }
}