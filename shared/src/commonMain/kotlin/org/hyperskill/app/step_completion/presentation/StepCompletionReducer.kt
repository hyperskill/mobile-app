package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.presentation.mapper.LearningActivityTargetViewActionMapper
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedStartPracticingHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalShownHyperskillAnalyticEvent
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
            // TODO: ALTAPPS-596: Project stage completion
            // TODO: ALTAPPS-610: Progress page
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