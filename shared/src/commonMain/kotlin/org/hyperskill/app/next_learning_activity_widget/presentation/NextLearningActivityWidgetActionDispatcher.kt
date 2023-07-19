package org.hyperskill.app.next_learning_activity_widget.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.Action
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.InternalAction
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.InternalMessage
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.Message
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class NextLearningActivityWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        nextLearningActivityStateRepository.changes
            .distinctUntilChanged()
            .onEach { onNewMessage(InternalMessage.NextLearningActivityChanged(it)) }
            .launchIn(actionScope)

        currentStudyPlanStateRepository.changes
            .distinctUntilChanged()
            .onEach { onNewMessage(InternalMessage.StudyPlanChanged(it)) }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchNextLearningActivity -> {
                handleFetchNextLearningActivityAction(action, ::onNewMessage)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
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

        coroutineScope {
            val nextLearningActivityDeferred = async {
                nextLearningActivityStateRepository.getState(forceUpdate = action.forceUpdate)
            }
            val currentStudyPlanDeferred = async {
                currentStudyPlanStateRepository.getState(forceUpdate = false)
            }

            val nextLearningActivity = nextLearningActivityDeferred.await()
                .getOrElse {
                    sentryInteractor.finishTransaction(transaction, throwable = it)
                    return@coroutineScope onNewMessage(InternalMessage.FetchNextLearningActivityDataError)
                }
            val currentStudyPlan = currentStudyPlanDeferred.await().getOrNull()

            sentryInteractor.finishTransaction(transaction)
            onNewMessage(InternalMessage.FetchNextLearningActivityDataSuccess(nextLearningActivity, currentStudyPlan))
        }
    }
}