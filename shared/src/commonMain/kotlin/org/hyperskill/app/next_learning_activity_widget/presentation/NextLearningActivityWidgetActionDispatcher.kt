package org.hyperskill.app.next_learning_activity_widget.presentation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.Action
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.InternalAction
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.InternalMessage
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.Message
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class NextLearningActivityWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        nextLearningActivityStateRepository.changes
            .distinctUntilChanged()
            .onEach { onNewMessage(InternalMessage.NextLearningActivityChanged(it)) }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchNextLearningActivity -> {
                handleFetchNextLearningActivityAction(action, ::onNewMessage)
            }
        }
    }

    private suspend fun handleFetchNextLearningActivityAction(
        action: InternalAction.FetchNextLearningActivity,
        onNewMessage: (Message) -> Unit
    ) {
        val transaction = HyperskillSentryTransactionBuilder
            .buildNextLearningActivityWidgetFetchNextLearningActivity()
        sentryInteractor.startTransaction(transaction)

        nextLearningActivityStateRepository
            .getState(forceUpdate = action.forceUpdate)
            .fold(
                onSuccess = { nextLearningActivity ->
                    sentryInteractor.finishTransaction(transaction)
                    onNewMessage(InternalMessage.FetchNextLearningActivitySuccess(nextLearningActivity))
                },
                onFailure = { throwable ->
                    sentryInteractor.finishTransaction(transaction, throwable = throwable)
                    onNewMessage(InternalMessage.FetchNextLearningActivityError)
                }
            )
    }
}