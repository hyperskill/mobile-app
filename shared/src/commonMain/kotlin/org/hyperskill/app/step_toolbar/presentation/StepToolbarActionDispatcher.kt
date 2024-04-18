package org.hyperskill.app.step_toolbar.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.Action
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.InternalMessage
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    private val progressesInteractor: ProgressesInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is StepToolbarFeature.InternalAction.FetchTopicProgress -> {
                val message = progressesInteractor
                    .getTopicProgress(action.topicId)
                    .fold(
                        onSuccess = { InternalMessage.FetchTopicProgressSuccess(it) },
                        onFailure = { InternalMessage.FetchTopicProgressError }
                    )
                onNewMessage(message)
            }
        }
    }
}