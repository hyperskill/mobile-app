package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

interface StepCompletionFeature {
    companion object {
        fun createState(step: Step, stepRoute: StepRoute): State =
            State(
                currentStep = step,
                continueButtonAction = if (stepRoute is StepRoute.Learn) {
                    ContinueButtonAction.FetchNextStepQuiz
                } else {
                    ContinueButtonAction.NavigateToBack
                }
            )
    }

    data class State(
        val currentStep: Step,
        val continueButtonAction: ContinueButtonAction,
        val isPracticingLoading: Boolean = false,
        val nextStepRoute: StepRoute? = null
    )

    sealed interface ContinueButtonAction {
        object NavigateToHomeScreen : ContinueButtonAction
        object NavigateToBack : ContinueButtonAction
        object FetchNextStepQuiz : ContinueButtonAction
        object CheckTopicCompletion : ContinueButtonAction
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