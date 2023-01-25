package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

interface StepCompletionFeature {
    data class State(
        val currentStep: Step,
        val continueButtonAction: ContinueButtonAction,
        val isPracticingLoading: Boolean = false
    )

    sealed interface ContinueButtonAction {
        object NavigateToHomeScreen : ContinueButtonAction
        object NavigateToBack : ContinueButtonAction
        object FetchNextStepQuiz : ContinueButtonAction
    }

    sealed interface Message {
        object StartPracticingClicked : Message

        object ContinuePracticingClicked : Message

        data class StepSolved(val stepId: Long) : Message

        /**
         * Topic completed modal
         */

        sealed interface CurrentTopicStatus : Message {
            data class Completed(val modalText: String) : CurrentTopicStatus
            object Uncompleted : CurrentTopicStatus
        }

        object TopicCompletedModalGoToHomeScreenClicked : Message

        sealed interface NextStepQuizFetchedStatus : Message {
            data class Success(val newStepRoute: StepRoute) : NextStepQuizFetchedStatus
            data class Error(val errorMessage: String) : NextStepQuizFetchedStatus
        }

        /**
         * Analytic
         */
        object TopicCompletedModalShownEventMessage : Message
        object TopicCompletedModalHiddenEventMessage : Message
    }

    sealed interface Action {
        data class FetchNextStepQuiz(val currentStep: Step) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class CheckTopicCompletion(val topicId: Long) : Action

        sealed interface ViewAction : Action {
            data class ShowTopicCompletedModal(val modalText: String) : ViewAction

            data class ShowPracticingErrorStatus(val errorMessage: String) : ViewAction

            data class ReloadStep(val stepRoute: StepRoute) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
                object HomeScreen : NavigateTo
            }
        }
    }
}