package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.presentation.mapper.LearningActivityTargetViewActionMapper
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedStartPracticingHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalClickedShareStreakHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionInterviewPreparationCompletedModalClickedGoTrainingHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionInterviewPreparationCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionInterviewPreparationCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShareStreakModalClickedNoThanksHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShareStreakModalClickedShareHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShareStreakModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShareStreakModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.ContinueButtonAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalMessage
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.State
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias StepCompletionReducerResult = Pair<State, Set<Action>>

class StepCompletionReducer(private val stepRoute: StepRoute) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.ContinuePracticingClicked ->
                handleContinuePracticingClickedMessage(state)
            is Message.StartPracticingClicked ->
                if (!state.isPracticingLoading) {
                    state.copy(isPracticingLoading = true) to setOf(
                        Action.LogAnalyticEvent(
                            StepCompletionClickedStartPracticingHyperskillAnalyticEvent(
                                route = stepRoute.analyticRoute
                            )
                        ),
                        when (state.startPracticingButtonAction) {
                            StepCompletionFeature.StartPracticingButtonAction.FetchNextStepQuiz ->
                                Action.FetchNextRecommendedStep(state.currentStep)
                            StepCompletionFeature.StartPracticingButtonAction.NavigateToBack ->
                                Action.ViewAction.NavigateTo.Back
                        }
                    )
                } else {
                    null
                }
            is Message.FetchNextRecommendedStepResult.Success ->
                state.copy(isPracticingLoading = false) to setOf(Action.ViewAction.ReloadStep(message.newStepRoute))
            is Message.FetchNextRecommendedStepResult.Error ->
                state.copy(isPracticingLoading = false) to setOf(
                    Action.ViewAction.ShowStartPracticingError(message.errorMessage)
                )
            is Message.CheckTopicCompletionStatus.Completed -> {
                val nextStepRoute = getNextStepRouteForLearningActivity(message.nextLearningActivity)
                state.copy(
                    continueButtonAction = ContinueButtonAction.NavigateToStudyPlan,
                    isPracticingLoading = false,
                    nextStepRoute = nextStepRoute
                ) to setOf(
                    Action.LogTopicCompletedAnalyticEvent(topicId = message.topicId),
                    Action.ViewAction.ShowTopicCompletedModal(
                        modalText = message.modalText,
                        isNextStepAvailable = nextStepRoute != null
                    )
                )
            }
            is Message.CheckTopicCompletionStatus.Uncompleted ->
                state to setOf(Action.FetchNextRecommendedStep(currentStep = state.currentStep))
            is Message.CheckTopicCompletionStatus.Error ->
                state.copy(isPracticingLoading = false) to setOf(
                    Action.ViewAction.ShowStartPracticingError(message.errorMessage)
                )
            is Message.TopicCompletedModalGoToStudyPlanClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.StudyPlan,
                    Action.LogAnalyticEvent(
                        StepCompletionTopicCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute
                        )
                    )
                )
            is Message.TopicCompletedModalContinueNextTopicClicked ->
                if (state.nextStepRoute != null) {
                    state to setOf(
                        Action.ViewAction.ReloadStep(state.nextStepRoute),
                        Action.LogAnalyticEvent(
                            StepCompletionTopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent(
                                route = stepRoute.analyticRoute
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.ProblemOfDaySolved ->
                state to setOf(
                    Action.ViewAction.ShowProblemOfDaySolvedModal(
                        earnedGemsText = message.earnedGemsText,
                        shareStreakData = message.shareStreakData
                    )
                )
            is Message.ProblemOfDaySolvedModalGoBackClicked -> {
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute
                        )
                    ),
                    Action.ViewAction.NavigateTo.Back
                )
            }
            is Message.ProblemOfDaySolvedModalShareStreakClicked -> {
                state to setOf(
                    Action.UpdateLastTimeShareStreakShown,
                    Action.LogAnalyticEvent(
                        StepCompletionDailyStepCompletedModalClickedShareStreakHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    ),
                    Action.ViewAction.ShowShareStreakSystemModal(streak = message.streak)
                )
            }
            is Message.StepSolved ->
                handleStepSolvedMessage(state, message)
            is Message.ShareStreak -> {
                state to setOf(Action.ViewAction.ShowShareStreakModal(streak = message.streak))
            }
            is Message.ShareStreakModalShownEventMessage -> {
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionShareStreakModalShownHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    )
                )
            }
            is Message.ShareStreakModalHiddenEventMessage -> {
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionShareStreakModalHiddenHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    )
                )
            }
            is Message.ShareStreakModalShareClicked -> {
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionShareStreakModalClickedShareHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    ),
                    Action.ViewAction.ShowShareStreakSystemModal(streak = message.streak)
                )
            }
            is Message.ShareStreakModalNoThanksClickedEventMessage -> {
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionShareStreakModalClickedNoThanksHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    )
                )
            }
            is InternalMessage.FetchNextInterviewStepResultSuccess ->
                handleFetchNextInterviewStepResultSuccessMessage(state, message)
            is InternalMessage.FetchNextInterviewStepResultError ->
                handleFetchNextInterviewStepResultErrorMessage(state, message)
            is Message.InterviewPreparationCompletedModalShownEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionInterviewPreparationCompletedModalShownHyperskillAnalyticEvent(
                            stepRoute.analyticRoute
                        )
                    )
                )
            is Message.InterviewPreparationCompletedModalHiddenEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionInterviewPreparationCompletedModalHiddenHyperskillAnalyticEvent(
                            stepRoute.analyticRoute
                        )
                    )
                )
            is Message.InterviewPreparationCompletedModalGoToTrainingClicked ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionInterviewPreparationCompletedModalClickedGoTrainingHyperskillAnalyticEvent(
                            stepRoute.analyticRoute
                        )
                    ),
                    Action.ViewAction.NavigateTo.Home
                )
            Message.RequestUserReview -> {
                state to setOf(Action.ViewAction.ShowRequestUserReviewModal(stepRoute))
            }
            /**
             * Analytic
             * */
            is Message.TopicCompletedModalShownEventMessage -> {
                val event = StepCompletionTopicCompletedModalShownHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state to setOf(Action.LogAnalyticEvent(event))
            }
            is Message.TopicCompletedModalHiddenEventMessage -> {
                val event = StepCompletionTopicCompletedModalHiddenHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state to setOf(Action.LogAnalyticEvent(event))
            }
            is Message.DailyStepCompletedModalShownEventMessage -> {
                val event = StepCompletionDailyStepCompletedModalShownHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state to setOf(Action.LogAnalyticEvent(event))
            }
            is Message.DailyStepCompletedModalHiddenEventMessage -> {
                val event = StepCompletionDailyStepCompletedModalHiddenHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state to setOf(Action.LogAnalyticEvent(event))
            }
        } ?: (state to emptySet())

    private fun handleContinuePracticingClickedMessage(state: State) =
        if (!state.isPracticingLoading) {
            val analyticEvent = StepCompletionClickedContinueHyperskillAnalyticEvent(
                route = stepRoute.analyticRoute
            )
            state.copy(
                isPracticingLoading = when (state.continueButtonAction) {
                    is ContinueButtonAction.CheckTopicCompletion,
                    ContinueButtonAction.FetchNextInterviewStep ->
                        true
                    ContinueButtonAction.NavigateToBack,
                    ContinueButtonAction.NavigateToStudyPlan ->
                        false
                }
            ) to setOf(
                Action.LogAnalyticEvent(analyticEvent),
                when (state.continueButtonAction) {
                    ContinueButtonAction.NavigateToBack -> Action.ViewAction.NavigateTo.Back
                    ContinueButtonAction.NavigateToStudyPlan -> Action.ViewAction.NavigateTo.StudyPlan
                    ContinueButtonAction.CheckTopicCompletion -> state.currentStep.topic?.let {
                        Action.CheckTopicCompletionStatus(it)
                    } ?: Action.ViewAction.NavigateTo.Back
                    ContinueButtonAction.FetchNextInterviewStep -> InternalAction.FetchNextInterviewStep
                }
            )
        } else {
            null
        }

    private fun handleFetchNextInterviewStepResultSuccessMessage(
        state: State,
        message: InternalMessage.FetchNextInterviewStepResultSuccess
    ): StepCompletionReducerResult =
        if (message.interviewStepId != null) {
            state.copy(isPracticingLoading = false) to
                setOf(
                    Action.ViewAction.ReloadStep(
                        StepRoute.InterviewPreparation(message.interviewStepId)
                    )
                )
        } else {
            state.copy(
                isPracticingLoading = false,
                continueButtonAction = ContinueButtonAction.NavigateToStudyPlan
            ) to setOf(Action.ViewAction.ShowInterviewPreparationCompletedModal)
        }

    private fun handleFetchNextInterviewStepResultErrorMessage(
        state: State,
        message: InternalMessage.FetchNextInterviewStepResultError
    ) =
        state.copy(isPracticingLoading = false) to
            setOf(Action.ViewAction.ShowStartPracticingError(message.errorMessage))

    private fun handleStepSolvedMessage(
        state: State,
        message: Message.StepSolved
    ): StepCompletionReducerResult =
        if (message.stepId == state.currentStep.id) {
            state to buildSet {
                if (StepQuizResolver.isStepHasLimitedAttempts(stepRoute)) {
                    add(Action.UpdateProblemsLimit(FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION))
                }

                if (stepRoute is StepRoute.InterviewPreparation) {
                    add(InternalAction.MarkInterviewStepAsSolved(message.stepId))
                }
            }
        } else {
            state to emptySet()
        }

    private fun getNextStepRouteForLearningActivity(learningActivity: LearningActivity?): StepRoute? {
        if (learningActivity == null) {
            return null
        }

        val learningActivityTargetViewAction = LearningActivityTargetViewActionMapper
            .mapLearningActivityToTargetViewAction(
                activity = learningActivity,
                trackId = null,
                projectId = null
            )
            .getOrElse { return null }

        return if (learningActivityTargetViewAction is LearningActivityTargetViewAction.NavigateTo.Step) {
            learningActivityTargetViewAction.stepRoute
        } else {
            null
        }
    }
}