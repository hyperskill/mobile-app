package org.hyperskill.app.step_completion.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.notification.data.extension.NotificationExtensions
import org.hyperskill.app.notification.domain.interactor.NotificationInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
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
    private val profileInteractor: ProfileInteractor,
    private val notificationInteractor: NotificationInteractor,
    private val topicCompletedFlow: TopicCompletedFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        notificationInteractor.solvedStepsSharedFlow
            .onEach { solvedStepId ->
                if (notificationInteractor.isRequiredToAskUserToEnableDailyReminders()) {
                    onNewMessage(Message.RequestDailyStudyRemindersPermission)
                } else {
                    val cachedProfile = profileInteractor
                        .getCurrentProfile(sourceType = DataSourceType.CACHE)
                        .getOrElse { return@onEach }

                    if (cachedProfile.dailyStep == solvedStepId) {
                        val currentProfileHypercoinsBalance = profileInteractor
                            .getCurrentProfile(sourceType = DataSourceType.REMOTE)
                            .map { it.gamification.hypercoinsBalance }
                            .getOrElse { return@onEach }

                        val gemsEarned = currentProfileHypercoinsBalance - cachedProfile.gamification.hypercoinsBalance

                        profileInteractor.notifyHypercoinsBalanceChanged(currentProfileHypercoinsBalance)
                        onNewMessage(
                            Message.ShowProblemOfDaySolvedModal(
                                earnedGemsText = resourceProvider.getQuantityString(SharedResources.plurals.earned_gems, gemsEarned, gemsEarned)
                            )
                        )
                    } else {
                        onNewMessage(Message.StepSolved(solvedStepId))
                    }
                }
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
                    topicCompletedFlow.notifyDataChanged(action.topicId)

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
            is Action.RequestDailyStudyRemindersPermissionResult -> {
                if (action.isGranted) {
                    notificationInteractor.setDailyStudyRemindersEnabled(true)
                    notificationInteractor.setDailyStudyRemindersIntervalStartHour(
                        NotificationExtensions.DAILY_REMINDERS_AFTER_STEP_SOLVED_START_HOUR
                    )
                } else {
                    notificationInteractor.setLastTimeUserAskedToEnableDailyReminders(
                        Clock.System.now().toEpochMilliseconds()
                    )
                }
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}