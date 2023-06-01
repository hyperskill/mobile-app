package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

interface StepCompletionFeature {
    companion object {
        fun createState(step: Step, stepRoute: StepRoute): State =
            State(
                currentStep = step,
                startPracticingAction = when (stepRoute) {
                    is StepRoute.Learn.TheoryOpenedFromPractice, is StepRoute.Repeat.Theory ->
                        StartPracticingAction.NavigateToBack
                    is StepRoute.Learn.Step, is StepRoute.LearnDaily,
                    is StepRoute.Repeat.Practice, is StepRoute.StageImplement ->
                        StartPracticingAction.FetchNextStepQuiz
                },
                continuePracticingAction = if (stepRoute is StepRoute.Learn) {
                    ContinuePracticingAction.FetchNextStepQuiz
                } else {
                    ContinuePracticingAction.NavigateToBack
                }
            )
    }

    data class State(
        val currentStep: Step,
        val startPracticingAction: StartPracticingAction,
        val continuePracticingAction: ContinuePracticingAction,
        val isPracticingLoading: Boolean = false,
        val nextStepRoute: StepRoute? = null
    )

    sealed interface StartPracticingAction {
        object NavigateToBack : StartPracticingAction
        object FetchNextStepQuiz : StartPracticingAction
    }

    sealed interface ContinuePracticingAction {
        object NavigateToHomeScreen : ContinuePracticingAction
        object NavigateToBack : ContinuePracticingAction
        object FetchNextStepQuiz : ContinuePracticingAction
        object CheckTopicCompletion : ContinuePracticingAction
    }

    sealed interface Message {
        object StartPracticingClicked : Message

        object ContinuePracticingClicked : Message

        data class StepSolved(val stepId: Long) : Message

        /**
         * Topic completed modal
         */

        sealed interface CheckTopicCompletionStatus : Message {
            data class Completed(val modalText: String, val nextStepId: Long?) : CheckTopicCompletionStatus
            object Uncompleted : CheckTopicCompletionStatus
            object Error : CheckTopicCompletionStatus
        }

        object TopicCompletedModalGoToHomeScreenClicked : Message
        object TopicCompletedModalContinueNextTopicClicked : Message

        sealed interface FetchNextRecommendedStepResult : Message {
            data class Success(val newStepRoute: StepRoute) : FetchNextRecommendedStepResult
            data class Error(val errorMessage: String) : FetchNextRecommendedStepResult
        }

        /**
         * Analytic
         */
        object TopicCompletedModalShownEventMessage : Message
        object TopicCompletedModalHiddenEventMessage : Message
    }

    sealed interface Action {
        data class FetchNextRecommendedStep(val currentStep: Step) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class CheckTopicCompletionStatus(val topicId: Long) : Action

        object UpdateProblemsLimit : Action

        sealed interface ViewAction : Action {
            data class ShowTopicCompletedModal(val modalText: String, val isNextStepAvailable: Boolean) : ViewAction

            data class ShowStartPracticingError(val message: String) : ViewAction

            data class ReloadStep(val stepRoute: StepRoute) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
                object HomeScreen : NavigateTo
            }
        }
    }
}