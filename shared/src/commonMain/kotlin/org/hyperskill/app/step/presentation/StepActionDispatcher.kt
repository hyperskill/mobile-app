package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchStep -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                stepInteractor
                    .getStep(action.stepRoute.stepId)
                    .fold(
                        onSuccess = {
                            sentryInteractor.finishTransaction(sentryTransaction)
                            onNewMessage(Message.StepLoaded.Success(it))
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            onNewMessage(Message.StepLoaded.Error)
                        }
                    )
            }
            is Action.ViewStep -> {
                stepInteractor.viewStep(action.stepId, action.stepContext)
            }
            is Action.UpdateNextLearningActivityState -> {
                handleUpdateNextLearningActivityStateAction(action)
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }

    private suspend fun handleUpdateNextLearningActivityStateAction(action: Action.UpdateNextLearningActivityState) {
        val currentNextLearningActivityState = nextLearningActivityStateRepository
            .getStateWithSource(forceUpdate = false)
            .getOrElse { return }

        if (currentNextLearningActivityState.usedDataSourceType == DataSourceType.REMOTE) {
            return
        }

        val isInTheSameTopic = currentNextLearningActivityState.state?.topicId == action.step.topic
        val isStepNotCurrent = currentNextLearningActivityState.state?.targetId != action.step.id

        if (isInTheSameTopic && isStepNotCurrent) {
            nextLearningActivityStateRepository.reloadState()
        }
    }
}