package org.hyperskill.app.step_quiz_toolbar.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.Action
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.InternalAction
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.InternalMessage
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.Message
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        currentSubscriptionStateRepository
            .changes
            .distinctUntilChanged()
            .onEach {
                onNewMessage(InternalMessage.SubscriptionChanged(it))
            }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event)
            is InternalAction.FetchSubscription -> handleFetchSubscription(::onNewMessage)
        }
    }

    private suspend fun handleFetchSubscription(onNewMessage: (Message) -> Unit) {
        currentSubscriptionStateRepository
            .getState()
            .fold(
                onSuccess = {
                    onNewMessage(InternalMessage.SubscriptionFetchSuccess(it))
                },
                onFailure = {
                    logger.e(it) { "Failed to fetch subscription" }
                    onNewMessage(InternalMessage.SubscriptionFetchError)
                }
            )
    }
}