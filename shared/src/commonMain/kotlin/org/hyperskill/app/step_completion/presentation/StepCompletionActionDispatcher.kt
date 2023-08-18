package org.hyperskill.app.step_completion.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.notification.local.cache.NotificationCacheKeyValues
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
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
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepCompletionActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val topicsInteractor: TopicsInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val resourceProvider: ResourceProvider,
    private val sentryInteractor: SentryInteractor,
    private val freemiumInteractor: FreemiumInteractor,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val topicCompletedFlow: TopicCompletedFlow,
    private val topicProgressFlow: TopicProgressFlow,
    private val notificationInteractor: NotificationInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        notificationInteractor.solvedStepsSharedFlow
            .onEach { solvedStepId ->
                handleStepSolved(solvedStepId)
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
                            Message.FetchNextRecommendedStepResult.Success(StepRoute.Learn.Step(it.id))
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
                handleCheckTopicCompletionStatusAction(action, ::onNewMessage)
            }
            is Action.UpdateProblemsLimit -> {
                freemiumInteractor.onStepSolved()
            }
            is Action.TurnOnDailyStudyReminder -> {
                handleTurnOnDailyStudyReminderAction()
            }
            is Action.PostponeDailyStudyReminder -> {
                handlePostponeDailyStudyReminderAction()
            }
            is Action.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleCheckTopicCompletionStatusAction(
        action: Action.CheckTopicCompletionStatus,
        onNewMessage: (Message) -> Unit
    ) {
        val topicProgress = progressesInteractor
            .getTopicProgress(action.topicId)
            .getOrElse { return onNewMessage(Message.CheckTopicCompletionStatus.Error) }

        if (topicProgress.isCompleted) {
            topicCompletedFlow.notifyDataChanged(action.topicId)

            coroutineScope {
                val topicDeferred = async {
                    topicsInteractor.getTopic(action.topicId)
                }
                val nextLearningActivityDeferred = async {
                    nextLearningActivityStateRepository.getState(forceUpdate = true)
                }

                val topic = topicDeferred.await()
                    .getOrElse { return@coroutineScope onNewMessage(Message.CheckTopicCompletionStatus.Error) }
                val nextLearningActivity = nextLearningActivityDeferred.await()
                    .getOrNull()

                onNewMessage(
                    Message.CheckTopicCompletionStatus.Completed(
                        modalText = resourceProvider.getString(
                            SharedResources.strings.step_quiz_topic_completed_modal_text,
                            topic.title
                        ),
                        nextLearningActivity = nextLearningActivity
                    )
                )
            }
        } else {
            topicProgressFlow.notifyDataChanged(topicProgress)
            onNewMessage(Message.CheckTopicCompletionStatus.Uncompleted)
        }
    }

    private suspend fun handleTurnOnDailyStudyReminderAction() {
        notificationInteractor.setDailyStudyRemindersEnabled(true)
        notificationInteractor.setDailyStudyReminderNotificationTime(
            NotificationCacheKeyValues.DAILY_STUDY_REMINDERS_START_HOUR_AFTER_STEP_SOLVED
        )
    }

    private fun handlePostponeDailyStudyReminderAction() {
        notificationInteractor.setLastTimeUserAskedToEnableDailyReminders(
            Clock.System.now().toEpochMilliseconds()
        )
    }

    private suspend fun handleStepSolved(stepId: Long) {
        // we should load cached and current profile
        // to update hypercoins balance in case of
        // requesting study reminders permission
        val cachedProfile = currentProfileStateRepository
            .getState(forceUpdate = false)
            .getOrElse { return }

        val currentProfile = currentProfileStateRepository
            .getState(forceUpdate = true)
            .getOrElse { return }

        val shouldDailyStudyRemindersPermissionBeRequested =
            !currentProfile.isDailyLearningNotificationEnabled &&
                notificationInteractor.isRequiredToAskUserToEnableDailyReminders()

        if (shouldDailyStudyRemindersPermissionBeRequested) {
            onNewMessage(Message.RequestDailyStudyRemindersPermission)
        } else {
            if (cachedProfile.dailyStep == stepId) {
                val gemsEarned =
                    currentProfile.gamification.hypercoinsBalance - cachedProfile.gamification.hypercoinsBalance
                onNewMessage(
                    Message.ProblemOfDaySolved(
                        // TODO move formatting to the view state mapper
                        earnedGemsText = resourceProvider.getQuantityString(
                            SharedResources.plurals.earned_gems,
                            gemsEarned,
                            gemsEarned
                        )
                    )
                )
            } else {
                onNewMessage(Message.StepSolved(stepId))
            }
        }
    }
}