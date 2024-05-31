package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.learning_activities.presentation.mapper.LearningActivityTargetViewActionMapper
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedStartPracticingHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalClickedShareStreakHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShareStreakModalClickedNoThanksHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShareStreakModalClickedShareHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShareStreakModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShareStreakModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.ContinueButtonAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalMessage
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.State
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias StepCompletionReducerResult = Pair<State, Set<Action>>

class StepCompletionReducer(private val stepRoute: StepRoute) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.ContinuePracticingClicked ->
                handleContinuePracticingClickedMessage(state)
            is Message.StartPracticingClicked ->
                handleStartPracticingClicked(state)
            is InternalMessage.FetchNextRecommendedStepSuccess ->
                state.copy(isPracticingLoading = false) to setOf(Action.ViewAction.ReloadStep(message.newStepRoute))
            is InternalMessage.FetchNextRecommendedStepError ->
                state.copy(isPracticingLoading = false) to setOf(
                    Action.ViewAction.ShowStartPracticingError(message.errorMessage)
                )
            is Message.CheckTopicCompletionStatus.Completed -> {
                val nextStepRoute =
                    message.nextLearningActivity
                        ?.let(LearningActivityTargetViewActionMapper::mapLearningActivityToStepRouteOrNull)
                state.copy(
                    continueButtonAction = ContinueButtonAction.NavigateToStudyPlan,
                    isPracticingLoading = false,
                    nextStepRoute = nextStepRoute
                ) to setOf(
                    InternalAction.LogTopicCompletedAnalyticEvent(topicId = message.topic.id),
                    Action.ViewAction.ShowTopicCompletedModal(
                        TopicCompletedModalFeatureParams(
                            topic = message.topic,
                            passedTopicsCount = message.passedTopicsCount,
                            canContinueWithNextTopic = nextStepRoute != null,
                            stepRoute = stepRoute
                        )
                    )
                )
            }
            is Message.CheckTopicCompletionStatus.Uncompleted ->
                state to setOf(InternalAction.FetchNextRecommendedStep(currentStep = state.currentStep))
            is Message.CheckTopicCompletionStatus.Error ->
                state.copy(isPracticingLoading = false) to setOf(
                    Action.ViewAction.ShowStartPracticingError(message.errorMessage)
                )
            is Message.TopicCompletedModalGoToStudyPlanClicked ->
                state to setOf(Action.ViewAction.NavigateTo.StudyPlan)
            is Message.TopicCompletedModalContinueNextTopicClicked ->
                if (state.nextStepRoute != null) {
                    state to setOf(Action.ViewAction.ReloadStep(state.nextStepRoute))
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
                    InternalAction.LogAnalyticEvent(
                        StepCompletionDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute
                        )
                    ),
                    Action.ViewAction.NavigateTo.Back
                )
            }
            is Message.ProblemOfDaySolvedModalShareStreakClicked -> {
                state to setOf(
                    InternalAction.UpdateLastTimeShareStreakShown,
                    InternalAction.LogAnalyticEvent(
                        StepCompletionDailyStepCompletedModalClickedShareStreakHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    ),
                    Action.ViewAction.ShowShareStreakSystemModal(streak = message.streak)
                )
            }
            is InternalMessage.StepSolved ->
                handleStepSolvedMessage(state, message)
            is Message.ShareStreak -> {
                state to setOf(Action.ViewAction.ShowShareStreakModal(streak = message.streak))
            }
            is Message.ShareStreakModalShownEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StepCompletionShareStreakModalShownHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    )
                )
            }
            is Message.ShareStreakModalHiddenEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StepCompletionShareStreakModalHiddenHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    )
                )
            }
            is Message.ShareStreakModalShareClicked -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
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
                    InternalAction.LogAnalyticEvent(
                        StepCompletionShareStreakModalClickedNoThanksHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            streak = message.streak
                        )
                    )
                )
            }
            Message.RequestUserReview -> {
                state to setOf(Action.ViewAction.ShowRequestUserReviewModal(stepRoute))
            }
            /**
             * Analytic
             * */
            is Message.DailyStepCompletedModalShownEventMessage -> {
                val event = StepCompletionDailyStepCompletedModalShownHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state to setOf(InternalAction.LogAnalyticEvent(event))
            }
            is Message.DailyStepCompletedModalHiddenEventMessage -> {
                val event = StepCompletionDailyStepCompletedModalHiddenHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state to setOf(InternalAction.LogAnalyticEvent(event))
            }
        } ?: (state to emptySet())

    private fun handleContinuePracticingClickedMessage(state: State) =
        if (!state.isPracticingLoading) {
            val analyticEvent = StepCompletionClickedContinueHyperskillAnalyticEvent(
                route = stepRoute.analyticRoute
            )
            state.copy(
                isPracticingLoading = when (state.continueButtonAction) {
                    is ContinueButtonAction.CheckTopicCompletion ->
                        true
                    ContinueButtonAction.NavigateToBack,
                    ContinueButtonAction.NavigateToStudyPlan ->
                        false
                }
            ) to setOf(
                InternalAction.LogAnalyticEvent(analyticEvent),
                when (state.continueButtonAction) {
                    ContinueButtonAction.NavigateToBack -> Action.ViewAction.NavigateTo.Back
                    ContinueButtonAction.NavigateToStudyPlan -> Action.ViewAction.NavigateTo.StudyPlan
                    ContinueButtonAction.CheckTopicCompletion -> state.currentStep.topic?.let {
                        InternalAction.CheckTopicCompletionStatus(it)
                    } ?: Action.ViewAction.NavigateTo.Back
                }
            )
        } else {
            null
        }

    private fun handleStartPracticingClicked(state: State) =
        if (!state.isPracticingLoading) {
            state.copy(isPracticingLoading = true) to setOf(
                InternalAction.LogAnalyticEvent(
                    StepCompletionClickedStartPracticingHyperskillAnalyticEvent(
                        route = stepRoute.analyticRoute
                    )
                ),
                when (state.startPracticingButtonAction) {
                    StepCompletionFeature.StartPracticingButtonAction.FetchNextStepQuiz ->
                        InternalAction.FetchNextRecommendedStep(state.currentStep)
                    StepCompletionFeature.StartPracticingButtonAction.NavigateToBack ->
                        Action.ViewAction.NavigateTo.Back
                }
            )
        } else {
            null
        }

    private fun handleStepSolvedMessage(
        state: State,
        message: InternalMessage.StepSolved
    ): StepCompletionReducerResult =
        if (message.stepId == state.currentStep.id) {
            state to buildSet {
                if (StepQuizResolver.isStepHasLimitedAttempts(stepRoute)) {
                    add(InternalAction.UpdateProblemsLimit(FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION))
                }
            }
        } else {
            state to emptySet()
        }
}