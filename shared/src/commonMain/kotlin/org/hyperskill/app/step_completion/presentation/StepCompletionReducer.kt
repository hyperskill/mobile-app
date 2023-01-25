package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedStartPracticingHyperskillAnalyticEvent
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
            is Message.ContinuePracticingClicked -> {
                val analyticEvent = StepCompletionClickedContinueHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state.copy(
                    isPracticingLoading = state.continueButtonAction is ContinueButtonAction.FetchNextStepQuiz
                ) to setOf(
                    Action.LogAnalyticEvent(analyticEvent),
                    when (state.continueButtonAction) {
                        ContinueButtonAction.NavigateToBack -> Action.ViewAction.NavigateTo.Back
                        ContinueButtonAction.NavigateToHomeScreen -> Action.ViewAction.NavigateTo.HomeScreen
                        ContinueButtonAction.FetchNextStepQuiz -> Action.FetchNextStepQuiz(
                            message.currentStep
                        )
                    }
                )
            }
            is Message.StartPracticingClicked -> {
                state.copy(isPracticingLoading = true) to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionClickedStartPracticingHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute
                        )
                    ),
                    Action.FetchNextStepQuiz(message.currentStep)
                )
            }
            is Message.NextStepQuizFetchedStatus.Success ->
                state.copy(isPracticingLoading = false) to setOf(Action.ViewAction.ReloadStep(message.newStepRoute))
            is Message.NextStepQuizFetchedStatus.Error ->
                state.copy(isPracticingLoading = false) to setOf(
                    Action.ViewAction.ShowPracticingErrorStatus(message.errorMessage)
                )
            is Message.CurrentTopicStatus.Completed ->
                state.copy(
                    continueButtonAction = ContinueButtonAction.NavigateToHomeScreen,
                    isPracticingLoading = false
                ) to setOf(Action.ViewAction.ShowTopicCompletedModal(message.modalText))
            is Message.CurrentTopicStatus.Uncompleted ->
                state.copy(isPracticingLoading = false) to emptySet()
            is Message.TopicCompletedModalGoToHomeScreenClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.HomeScreen,
                    Action.LogAnalyticEvent(
                        StepCompletionTopicCompletedModalClickedGoToHomeScreenHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute
                        )
                    )
                )
            is Message.StepSolved ->
                if (stepRoute is StepRoute.Learn && message.stepId == state.step.id) {
                    state.copy(isPracticingLoading = true) to setOf(Action.CheckTopicCompletion(state.step.topic))
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