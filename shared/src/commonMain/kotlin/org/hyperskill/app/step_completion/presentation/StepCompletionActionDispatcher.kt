package org.hyperskill.app.step_completion.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.flow.TopicProgressFlow
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.request_review.domain.interactor.RequestReviewInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.share_streak.domain.interactor.ShareStreakInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionStepSolvedAmplitudeAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionStepSolvedAppsFlyerAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedAmplitudeAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedAppsFlyerAnalyticEvent
import org.hyperskill.app.step_completion.domain.flow.DailyStepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalMessage
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.streaks.domain.model.StreakState
import org.hyperskill.app.subscriptions.domain.interactor.SubscriptionsInteractor
import org.hyperskill.app.topics.domain.repository.TopicsRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepCompletionActionDispatcher(
    config: ActionDispatcherOptions,
    stepCompletedFlow: StepCompletedFlow,
    private val stepInteractor: StepInteractor,
    private val progressesInteractor: ProgressesInteractor,
    private val topicsRepository: TopicsRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val resourceProvider: ResourceProvider,
    private val sentryInteractor: SentryInteractor,
    private val subscriptionsInteractor: SubscriptionsInteractor,
    private val shareStreakInteractor: ShareStreakInteractor,
    private val requestReviewInteractor: RequestReviewInteractor,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val currentGamificationToolbarDataStateRepository: CurrentGamificationToolbarDataStateRepository,
    private val dailyStepCompletedFlow: DailyStepCompletedFlow,
    private val topicCompletedFlow: TopicCompletedFlow,
    private val topicProgressFlow: TopicProgressFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        stepCompletedFlow.observe()
            .onEach(::handleStepSolved)
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchNextRecommendedStep -> {
                handleFetchNextRecommendedStepAction(action, ::onNewMessage)
            }
            is InternalAction.CheckTopicCompletionStatus -> {
                handleCheckTopicCompletionStatusAction(action, ::onNewMessage)
            }
            is InternalAction.UpdateProblemsLimit -> {
                subscriptionsInteractor.chargeProblemsLimits(action.chargeStrategy)
            }
            is InternalAction.UpdateLastTimeShareStreakShown -> {
                shareStreakInteractor.setLastTimeShareStreakShown()
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            is InternalAction.LogTopicCompletedAnalyticEvent -> {
                logTopicCompletedAnalyticEvents(action.topicId)
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchNextRecommendedStepAction(
        action: InternalAction.FetchNextRecommendedStep,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildStepCompletionNextStepLoading(),
            onError = { getFetchNextRecommendedStepErrorMessage(action.currentStep.type) }
        ) {
            val nextRecommendedStep = stepInteractor
                .getNextRecommendedStepAndCompleteCurrentIfNeeded(action.currentStep)
                .getOrThrow()
            if (nextRecommendedStep != null) {
                InternalMessage.FetchNextRecommendedStepSuccess(
                    StepRoute.Learn.Step(stepId = nextRecommendedStep.id, topicId = nextRecommendedStep.topic)
                )
            } else {
                getFetchNextRecommendedStepErrorMessage(action.currentStep.type)
            }
        }.let(onNewMessage)
    }

    private fun getFetchNextRecommendedStepErrorMessage(
        currentStepType: Step.Type
    ): InternalMessage.FetchNextRecommendedStepError =
        InternalMessage.FetchNextRecommendedStepError(
            when (currentStepType) {
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

    private suspend fun handleCheckTopicCompletionStatusAction(
        action: InternalAction.CheckTopicCompletionStatus,
        onNewMessage: (Message) -> Unit
    ) {
        val topicProgress = progressesInteractor
            .getTopicProgress(action.topicId, forceLoadFromRemote = true)
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

                val passedTopicsCount = currentProfileStateRepository
                    .getState(forceUpdate = false)
                    .map { it.gamification.passedTopicsCount }
                    .getOrElse { 0 }

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
                        topic = topic,
                        passedTopicsCount = passedTopicsCount,
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
        onNewMessage(InternalMessage.StepSolved(stepId))

        // We should load cached and current profile to update hypercoins balance in case of
        // requesting study reminders permission
        val cachedProfile = currentProfileStateRepository
            .getState(forceUpdate = false)
            .getOrElse { return }

        logStepCompletedAnalyticEvents(
            stepId = stepId,
            trackTitle = cachedProfile.trackTitle
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
                val earnedGemsText = if (gemsEarned > 0) {
                    resourceProvider.getQuantityString(
                        SharedResources.plurals.earned_gems,
                        gemsEarned,
                        gemsEarned
                    )
                } else {
                    null
                }

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

        val shouldRequestReview = requestReviewInteractor.shouldRequestReviewAfterStepSolved()

        if (shouldShareStreak && streakToShare != null) {
            shareStreakInteractor.setLastTimeShareStreakShown()
            onNewMessage(Message.ShareStreak(streak = streakToShare))
        } else if (shouldRequestReview) {
            requestReviewInteractor.handleRequestReviewPerformed()
            onNewMessage(Message.RequestUserReview)
        }

        updateCurrentProfileHypercoinsBalanceRemotely()
    }

    private suspend fun logStepCompletedAnalyticEvents(
        stepId: Long,
        trackTitle: String?
    ) {
        analyticInteractor.logEvent(
            StepCompletionStepSolvedAppsFlyerAnalyticEvent(
                stepId = stepId,
                trackTitle = trackTitle
            )
        )
        analyticInteractor.logEvent(
            StepCompletionStepSolvedAmplitudeAnalyticEvent(
                stepId = stepId,
                trackTitle = trackTitle
            )
        )
    }

    private suspend fun logTopicCompletedAnalyticEvents(topicId: Long) {
        val trackTitle = currentProfileStateRepository
            .getState(forceUpdate = false)
            .map { it.trackTitle }
            .getOrNull()

        analyticInteractor.logEvent(
            StepCompletionTopicCompletedAppsFlyerAnalyticEvent(
                topicId = topicId,
                trackTitle = trackTitle,
                trackIsCompleted = false
            )
        )
        analyticInteractor.logEvent(
            StepCompletionTopicCompletedAmplitudeAnalyticEvent(
                topicId = topicId,
                trackTitle = trackTitle
            )
        )
    }

    private suspend fun updateCurrentProfileHypercoinsBalanceRemotely(): Int? =
        currentProfileStateRepository
            .getState(forceUpdate = true)
            .map { it.gamification.hypercoinsBalance }
            .getOrNull()
}