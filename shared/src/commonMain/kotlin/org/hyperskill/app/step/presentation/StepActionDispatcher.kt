package org.hyperskill.app.step.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.domain.model.supportedBlocksNames
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.PracticeStatus
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val failedToLoadNextStepQuizMutableSharedFlow: MutableSharedFlow<Unit>
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        actionScope.launch {
            stepInteractor.observeReadyToLoadNextStepQuiz().collect { currentStep ->
                doSuspendableAction(Action.FetchNextStepQuiz(currentStep))
            }
        }
    }
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
                            onNewMessage(
                                Message.StepLoaded.Success(
                                    step = it,
                                    practiceStatus = if (action.stepRoute is StepRoute.Learn) {
                                        PracticeStatus.AVAILABLE
                                    } else {
                                        PracticeStatus.UNAVAILABLE
                                    }
                                )
                            )
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            onNewMessage(Message.StepLoaded.Error)
                        }
                    )
            }
            is Action.FetchNextStepQuiz -> {
                if (action.currentStep.type == Step.Type.THEORY && !action.currentStep.isCompleted) {
                    stepInteractor.completeStep(action.currentStep.id)
                }

                val nextRecommendedStep = stepInteractor
                    .getNextRecommendedStepByTopicId(action.currentStep.topic)
                    .getOrElse {
                        onNewMessage(Message.NextStepQuizFetchedStatus.Error)
                        failedToLoadNextStepQuizMutableSharedFlow.emit(Unit)
                        return
                    }

                while (!BlockName.supportedBlocksNames.contains(nextRecommendedStep.block.name) && nextRecommendedStep.canSkip) {
                    // TODO: Implement skip logic
                    break
                }

                onNewMessage(Message.NextStepQuizFetchedStatus.Success(StepRoute.Learn(nextRecommendedStep.id)))
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}