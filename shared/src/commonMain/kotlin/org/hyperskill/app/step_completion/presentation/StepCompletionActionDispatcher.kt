package org.hyperskill.app.step_completion.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepCompletionActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val topicsInteractor: TopicsInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val resourceProvider: ResourceProvider,
    private val sentryInteractor: SentryInteractor,
    notificationInteractor: NotificationInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        notificationInteractor.solvedStepsSharedFlow
            .onEach { stepId ->
                onNewMessage(Message.StepSolved(stepId))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchNextRecommendedStep -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStepCompletionNextStepLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val message = stepInteractor
                    .getNextRecommendedStepAndCompleteCurrentIfNeeded(action.currentStep)
                    .fold(
                        onSuccess = {
                            sentryInteractor.finishTransaction(sentryTransaction)
                            Message.FetchNextRecommendedStepResult.Success(StepRoute.Learn(it.id))
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            Message.FetchNextRecommendedStepResult.Error(
                                when (action.currentStep.type) {
                                    Step.Type.THEORY ->
                                        resourceProvider.getString(SharedResources.strings.step_theory_failed_to_start_practicing)
                                    Step.Type.PRACTICE ->
                                        resourceProvider.getString(SharedResources.strings.step_theory_failed_to_continue_practicing)
                                }
                            )
                        }
                    )

                onNewMessage(message)
            }
            is Action.CheckTopicCompletionStatus -> {
                val topicIsCompleted = progressesInteractor
                    .getTopicProgress(action.topicId)
                    .map { it.isCompleted }
                    .getOrElse { return onNewMessage(Message.CheckTopicCompletionStatus.Error) }

                if (topicIsCompleted) {
                    val topicTitle = topicsInteractor
                        .getTopic(action.topicId)
                        .map { it.title }
                        .getOrElse { return onNewMessage(Message.CheckTopicCompletionStatus.Error) }

                    onNewMessage(
                        Message.CheckTopicCompletionStatus.Completed(
                            resourceProvider.getString(
                                SharedResources.strings.step_quiz_topic_completed_modal_text,
                                topicTitle
                            )
                        )
                    )
                } else {
                    onNewMessage(Message.CheckTopicCompletionStatus.Uncompleted)
                }
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}