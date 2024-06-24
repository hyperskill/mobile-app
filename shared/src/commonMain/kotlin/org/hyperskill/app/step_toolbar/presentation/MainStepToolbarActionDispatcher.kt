package org.hyperskill.app.step_toolbar.presentation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.Action
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.InternalAction
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.InternalMessage
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainStepToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    topicCompletedFlow: TopicCompletedFlow,
    private val progressesInteractor: ProgressesInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        topicCompletedFlow.observe()
            .distinctUntilChanged()
            .onEach { topicId ->
                onNewMessage(InternalMessage.TopicCompleted(topicId))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchTopicProgress ->
                handleFetchTopicProgressAction(action, ::onNewMessage)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchTopicProgressAction(
        action: InternalAction.FetchTopicProgress,
        onNewMessage: (Message) -> Unit
    ) {
        val message = progressesInteractor
            .getTopicProgress(action.topicId, action.forceLoadFromRemote)
            .fold(
                onSuccess = { InternalMessage.FetchTopicProgressSuccess(it) },
                onFailure = { InternalMessage.FetchTopicProgressError }
            )
        onNewMessage(message)
    }
}