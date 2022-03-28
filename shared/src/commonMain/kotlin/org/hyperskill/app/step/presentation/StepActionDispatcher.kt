package org.hyperskill.app.step.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        // no op
    }
}