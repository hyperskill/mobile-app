package org.hyperskill.app.step_completion.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository
import org.hyperskill.app.interview_steps.domain.repository.InterviewStepsStateRepository
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.flow.TopicProgressFlow
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.share_streak.domain.interactor.ShareStreakInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionStepSolvedAppsFlyerAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedAppsFlyerAnalyticEvent
import org.hyperskill.app.step_completion.domain.flow.DailyStepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalMessage
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import org.hyperskill.app.streaks.domain.model.StreakState
import org.hyperskill.app.topics.domain.repository.TopicsRepository
import ru.nobird.app.core.model.mutate
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepCompletionActionDispatcher(
    config: ActionDispatcherOptions,
    submissionRepository: SubmissionRepository,
    private val stepInteractor: StepInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val topicsRepository: TopicsRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val resourceProvider: ResourceProvider,
    private val sentryInteractor: SentryInteractor,
    private val freemiumInteractor: FreemiumInteractor,
    private val shareStreakInteractor: ShareStreakInteractor,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val currentGamificationToolbarDataStateRepository: CurrentGamificationToolbarDataStateRepository,
    private val dailyStepCompletedFlow: DailyStepCompletedFlow,
    private val topicCompletedFlow: TopicCompletedFlow,
    private val topicProgressFlow: TopicProgressFlow,
    private val interviewStepsStateRepository: InterviewStepsStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        submissionRepository.solvedStepsSharedFlow
            .onEach(::handleStepSolved)
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
            is Action.UpdateLastTimeShareStreakShown -> {
                shareStreakInteractor.setLastTimeShareStreakShown()
            }
            is Action.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            is Action.LogTopicCompletedAnalyticEvent -> {
                val trackTitle = currentProfileStateRepository
                    .getState(forceUpdate = false)
                    .map { it.trackTitle }
                    .getOrNull()

                analyticInteractor.logEvent(
                    StepCompletionTopicCompletedAppsFlyerAnalyticEvent(
                        topicId = action.topicId,
                        trackTitle = trackTitle,
                        trackIsCompleted = false
                    )
                )
            }
            is InternalAction.FetchNextInterviewStep ->
                handleFetchNextInterviewStep(::onNewMessage)
            is InternalAction.MarkInterviewStepAsSolved ->
                handleMarkInterviewStepAsSolved(action)
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
            .getOrElse {
                return onNewMessage(
                    Message.CheckTopicCompletionStatus.Error(
                        resourceProvider.getString(
                            SharedResources.strings.step_theory_failed_to_continue_practicing
                        )
                    )
                )
            }

        if (topicProgress.isCompleted) {
            topicCompletedFlow.notifyDataChanged(action.topicId)

            coroutineScope {
                val topicDeferred = async {
                    topicsRepository.getTopic(action.topicId)
                }
                val nextLearningActivityDeferred = async {
                    nextLearningActivityStateRepository.getState(forceUpdate = true)
                }

                val topic = topicDeferred.await()
                    .getOrElse {
                        return@coroutineScope onNewMessage(
                            Message.CheckTopicCompletionStatus.Error(
                                resourceProvider.getString(
                                    SharedResources.strings.step_theory_failed_to_continue_practicing
                                )
                            )
                        )
                    }
                val nextLearningActivity = nextLearningActivityDeferred.await()
                    .getOrNull()

                onNewMessage(
                    Message.CheckTopicCompletionStatus.Completed(
                        topicId = action.topicId,
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

    private suspend fun handleStepSolved(stepId: Long) {
        // update problems limit
        onNewMessage(Message.StepSolved(stepId))

        // We should load cached and current profile to update hypercoins balance in case of
        // requesting study reminders permission
        val cachedProfile = currentProfileStateRepository
            .getState(forceUpdate = false)
            .getOrElse { return }

        analyticInteractor.logEvent(
            StepCompletionStepSolvedAppsFlyerAnalyticEvent(
                stepId = stepId,
                trackTitle = cachedProfile.trackTitle
            )
        )

        if (cachedProfile.dailyStep == stepId) {
            dailyStepCompletedFlow.notifyDataChanged(stepId)
        }

        val currentGamificationToolbarData = currentGamificationToolbarDataStateRepository
            .getState(forceUpdate = false)
            .getOrNull()
        val streakToShare = currentGamificationToolbarData?.let {
            if (it.streakState == StreakState.NOTHING) {
                it.currentStreak + 1
            } else {
                it.currentStreak
            }
        }
        val shouldShareStreak = if (currentGamificationToolbarData != null && streakToShare != null) {
            shareStreakInteractor.shouldShareStreakAfterStepSolved(
                streak = streakToShare,
                streakState = currentGamificationToolbarData.streakState
            )
        } else {
            false
        }

        if (cachedProfile.dailyStep == stepId) {
            val currentProfileHypercoinsBalance = updateCurrentProfileHypercoinsBalanceRemotely()
            if (currentProfileHypercoinsBalance != null) {
                val gemsEarned = currentProfileHypercoinsBalance - cachedProfile.gamification.hypercoinsBalance
                val earnedGemsText = resourceProvider.getQuantityString(
                    SharedResources.plurals.earned_gems,
                    gemsEarned,
                    gemsEarned
                )

                val shareStreakData = if (shouldShareStreak && streakToShare != null) {
                    val daysText = resourceProvider.getQuantityString(
                        SharedResources.plurals.days,
                        streakToShare,
                        streakToShare
                    )
                    StepCompletionFeature.ShareStreakData.Content(
                        streakText = resourceProvider.getString(
                            SharedResources.strings.step_quiz_problem_of_day_solved_modal_streak_text,
                            daysText
                        ),
                        streak = streakToShare
                    )
                } else {
                    StepCompletionFeature.ShareStreakData.Empty
                }

                onNewMessage(
                    Message.ProblemOfDaySolved(
                        earnedGemsText = earnedGemsText,
                        shareStreakData = shareStreakData
                    )
                )
                return
            }
        }

        if (shouldShareStreak && streakToShare != null) {
            shareStreakInteractor.setLastTimeShareStreakShown()
            onNewMessage(Message.ShareStreak(streak = streakToShare))

            updateCurrentProfileHypercoinsBalanceRemotely()
        }
    }

    private suspend fun updateCurrentProfileHypercoinsBalanceRemotely(): Int? =
        currentProfileStateRepository
            .getState(forceUpdate = true)
            .map { it.gamification.hypercoinsBalance }
            .getOrNull()

    private suspend fun handleFetchNextInterviewStep(
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildStepCompletionNextInterviewStepLoading(),
            onError = {
                InternalMessage.FetchNextInterviewStepResultError(
                    resourceProvider.getString(
                        SharedResources.strings.step_theory_failed_to_continue_practicing
                    )
                )
            }
        ) {
            InternalMessage.FetchNextInterviewStepResultSuccess(
                interviewStepsStateRepository
                    .getState(forceUpdate = false)
                    .getOrThrow()
                    .lastOrNull()
            )
        }.let(onNewMessage)
    }

    private suspend fun handleMarkInterviewStepAsSolved(action: InternalAction.MarkInterviewStepAsSolved) {
        interviewStepsStateRepository.updateState { steps ->
            steps.mutate { remove(action.stepId) }
        }
    }
}