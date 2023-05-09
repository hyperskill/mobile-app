package org.hyperskill.app.step_completion.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor
import org.hyperskill.app.progresses.domain.flow.TopicProgressFlow
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
import org.hyperskill.app.topics_to_discover_next.domain.interactor.TopicsToDiscoverNextInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepCompletionActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val topicsInteractor: TopicsInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val resourceProvider: ResourceProvider,
    private val sentryInteractor: SentryInteractor,
    private val topicsToDiscoverNextInteractor: TopicsToDiscoverNextInteractor,
    private val freemiumInteractor: FreemiumInteractor,
    private val topicCompletedFlow: TopicCompletedFlow,
    private val topicProgressFlow: TopicProgressFlow,
    notificationInteractor: NotificationInteractor,
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
                                        resourceProvider.getString(
                                            SharedResources.strings.step_theory_failed_to_start_practicing
                                        )
                                    Step.Type.PRACTICE ->
                                        resourceProvider.getString(
                                            SharedResources.strings.step_theory_failed_to_continue_practicing
                                        )
                                }
                            )
                        }
                    )

                onNewMessage(message)
            }
            is Action.CheckTopicCompletionStatus -> {
                val topicProgress = progressesInteractor
                    .getTopicProgress(action.topicId)
                    .getOrElse { return onNewMessage(Message.CheckTopicCompletionStatus.Error) }

                if (topicProgress.isCompleted) {
                    topicCompletedFlow.notifyDataChanged(action.topicId)

                    coroutineScope {
                        val topicResult = async {
                            topicsInteractor.getTopic(action.topicId)
                        }
                        val nextTopicToDiscoverResult = async {
                            topicsToDiscoverNextInteractor.getNextTopicToDiscover()
                        }

                        val topicTitle = topicResult.await()
                            .map { it.title }
                            .getOrElse {
                                onNewMessage(Message.CheckTopicCompletionStatus.Error)
                                return@coroutineScope
                            }
                        val nextStepId = nextTopicToDiscoverResult.await()
                            .getOrNull()
                            ?.theoryId

                        onNewMessage(
                            Message.CheckTopicCompletionStatus.Completed(
                                modalText = resourceProvider.getString(
                                    SharedResources.strings.step_quiz_topic_completed_modal_text,
                                    topicTitle
                                ),
                                nextStepId = nextStepId
                            )
                        )
                    }
                } else {
                    topicProgressFlow.notifyDataChanged(topicProgress)
                    onNewMessage(Message.CheckTopicCompletionStatus.Uncompleted)
                }
            }
            is Action.UpdateProblemsLimit ->
                freemiumInteractor.onStepSolved()
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}