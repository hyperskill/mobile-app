package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedStartPracticingHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.ContinuePracticingAction
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
                        isPracticingLoading =
                        state.continuePracticingAction is ContinuePracticingAction.FetchNextStepQuiz ||
                            state.continuePracticingAction is ContinuePracticingAction.CheckTopicCompletion
                    ) to setOf(
                        Action.LogAnalyticEvent(analyticEvent),
                        when (state.continuePracticingAction) {
                            ContinuePracticingAction.NavigateToBack -> Action.ViewAction.NavigateTo.Back
                            ContinuePracticingAction.NavigateToHomeScreen -> Action.ViewAction.NavigateTo.HomeScreen
                            ContinuePracticingAction.FetchNextStepQuiz -> Action.FetchNextRecommendedStep(
                                currentStep = state.currentStep
                            )
                            ContinuePracticingAction.CheckTopicCompletion -> state.currentStep.topic?.let {
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
                        when (state.startPracticingAction) {
                            StepCompletionFeature.StartPracticingAction.FetchNextStepQuiz ->
                                Action.FetchNextRecommendedStep(state.currentStep)
                            StepCompletionFeature.StartPracticingAction.NavigateToBack ->
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
            is Message.CheckTopicCompletionStatus.Completed ->
                state.copy(
                    continuePracticingAction = ContinuePracticingAction.NavigateToHomeScreen,
                    isPracticingLoading = false,
                    nextStepRoute = message.nextStepId?.let { StepRoute.Learn.Step(it) }
                ) to setOf(
                    Action.ViewAction.ShowTopicCompletedModal(
                        modalText = message.modalText,
                        isNextStepAvailable = message.nextStepId != null
                    )
                )
            is Message.CheckTopicCompletionStatus.Uncompleted ->
                state.copy(isPracticingLoading = false) to emptySet()
            is Message.CheckTopicCompletionStatus.Error ->
                state.copy(
                    continuePracticingAction = ContinuePracticingAction.CheckTopicCompletion,
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
}