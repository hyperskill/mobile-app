package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.PracticeStatus
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor,
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
                            onNewMessage(
                                Message.StepLoaded.Success(
                                    step = it,
                                    stepRoute = action.stepRoute,
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
            is Action.FetchPractice -> {
                if (!action.currentStep.isCompleted) {
                    stepInteractor.completeStep(action.currentStep.id)
                }
                val nextRecommendedStep = stepInteractor
                    .getNextRecommendedStepByTopicId(action.currentStep.topic)
                    .getOrElse {
                        onNewMessage(Message.PracticeFetchedError)
                        return
                    }

                onNewMessage(
                    Message.StepLoaded.Success(
                        step = nextRecommendedStep,
                        stepRoute = StepRoute.Learn(nextRecommendedStep.id),
                        practiceStatus = PracticeStatus.UNAVAILABLE
                    )
                )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}