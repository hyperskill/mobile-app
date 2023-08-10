package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.presentation.mapper.LearningActivityTargetViewActionMapper
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedStartPracticingHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionDailyStepCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionHiddenDailyNotificationsNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionShownDailyNotificationsNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionTopicCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.ContinueButtonAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepCompletionReducer(private val stepRoute: StepRoute) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.ContinuePracticingClicked ->
                if (!state.isPracticingLoading) {
                    val analyticEvent = StepCompletionClickedContinueHyperskillAnalyticEvent(
                        route = stepRoute.analyticRoute
                    )
                    state.copy(
                        isPracticingLoading = when (state.continueButtonAction) {
                            is ContinueButtonAction.FetchNextStepQuiz,
                            is ContinueButtonAction.CheckTopicCompletion ->
                                true
                            ContinueButtonAction.NavigateToBack,
                            ContinueButtonAction.NavigateToHomeScreen ->
                                false
                        }
                    ) to setOf(
                        Action.LogAnalyticEvent(analyticEvent),
                        when (state.continueButtonAction) {
                            ContinueButtonAction.NavigateToBack -> Action.ViewAction.NavigateTo.Back
                            ContinueButtonAction.NavigateToHomeScreen -> Action.ViewAction.NavigateTo.HomeScreen
                            ContinueButtonAction.FetchNextStepQuiz -> Action.FetchNextRecommendedStep(
                                currentStep = state.currentStep
                            )
                            ContinueButtonAction.CheckTopicCompletion -> state.currentStep.topic?.let {
                                Action.CheckTopicCompletionStatus(it)
                            } ?: Action.ViewAction.NavigateTo.Back
                        }
                    )
                } else {
                    null
                }
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
                    continueButtonAction = ContinueButtonAction.NavigateToHomeScreen,
                    isPracticingLoading = false,
                    nextStepRoute = nextStepRoute
                ) to setOf(
                    Action.ViewAction.ShowTopicCompletedModal(
                        modalText = message.modalText,
                        isNextStepAvailable = nextStepRoute != null
                    )
                )
            }
            is Message.CheckTopicCompletionStatus.Uncompleted ->
                state.copy(isPracticingLoading = false) to emptySet()
            is Message.CheckTopicCompletionStatus.Error ->
                state.copy(
                    continueButtonAction = ContinueButtonAction.CheckTopicCompletion,
                    isPracticingLoading = false
                ) to emptySet()
            is Message.TopicCompletedModalGoToHomeScreenClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.HomeScreen,
                    Action.LogAnalyticEvent(
                        StepCompletionTopicCompletedModalClickedGoToHomeScreenHyperskillAnalyticEvent(
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
                state to setOf(Action.ViewAction.ShowProblemOfDaySolvedModal(message.earnedGemsText))
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
            is Message.StepSolved ->
                if (!state.isPracticingLoading &&
                    stepRoute is StepRoute.Learn &&
                    message.stepId == state.currentStep.id &&
                    state.currentStep.topic != null
                ) {
                    state.copy(isPracticingLoading = true) to setOf(
                        Action.CheckTopicCompletionStatus(state.currentStep.topic),
                        Action.UpdateProblemsLimit
                    )
                } else {
                    null
                }
            Message.RequestDailyStudyRemindersPermission -> {
                state to setOfNotNull(
                    Action.ViewAction.RequestDailyStudyRemindersPermission,
                    Action.LogAnalyticEvent(
                        StepCompletionShownDailyNotificationsNoticeHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            }
            is Message.RequestDailyStudyRemindersPermissionResult -> {
                val analyticEvent = StepCompletionHiddenDailyNotificationsNoticeHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    isAgreed = message.isGranted
                )
                state to setOf(
                    if (message.isGranted) {
                        Action.TurnOnDailyStudyReminder
                    } else {
                        Action.PostponeDailyStudyReminder
                    },
                    Action.LogAnalyticEvent(analyticEvent)
                )
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