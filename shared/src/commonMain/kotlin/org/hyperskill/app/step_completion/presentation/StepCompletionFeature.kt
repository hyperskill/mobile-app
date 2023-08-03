package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

interface StepCompletionFeature {
    companion object {
        fun createState(step: Step, stepRoute: StepRoute): State =
            State(
                currentStep = step,
                startPracticingButtonAction = when (stepRoute) {
                    is StepRoute.Learn.TheoryOpenedFromPractice,
                    is StepRoute.Repeat.Theory ->
                        StartPracticingButtonAction.NavigateToBack
                    is StepRoute.Learn.Step,
                    is StepRoute.LearnDaily,
                    is StepRoute.Repeat.Practice,
                    is StepRoute.StageImplement ->
                        StartPracticingButtonAction.FetchNextStepQuiz
                },
                continueButtonAction = if (stepRoute is StepRoute.Learn) {
                    ContinueButtonAction.FetchNextStepQuiz
                } else {
                    ContinueButtonAction.NavigateToBack
                }
            )
    }

    data class State(
        val currentStep: Step,
        val startPracticingButtonAction: StartPracticingButtonAction,
        val continueButtonAction: ContinueButtonAction,
        val isPracticingLoading: Boolean = false,
        val nextStepRoute: StepRoute? = null
    )

    sealed interface StartPracticingButtonAction {
        object NavigateToBack : StartPracticingButtonAction
        object FetchNextStepQuiz : StartPracticingButtonAction
    }

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
            data class Completed(
                val modalText: String,
                val nextLearningActivity: LearningActivity?
            ) : CheckTopicCompletionStatus
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
         * Show problem of day solve modal
         */
        data class ShowProblemOfDaySolvedModal(val earnedGemsText: String) : Message
        object ProblemOfDaySolvedModalGoBackClicked : Message

        /**
         * Daily study reminders
         */
        object RequestDailyStudyRemindersPermission : Message
        data class RequestDailyStudyRemindersPermissionResult(val isGranted: Boolean) : Message

        /**
         * Analytic
         */
        object TopicCompletedModalShownEventMessage : Message
        object TopicCompletedModalHiddenEventMessage : Message
        object DailyStepCompletedModalShownEventMessage : Message
        object DailyStepCompletedModalHiddenEventMessage : Message
    }

    sealed interface Action {
        data class FetchNextRecommendedStep(val currentStep: Step) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class CheckTopicCompletionStatus(val topicId: Long) : Action

        object UpdateProblemsLimit : Action

        data class RequestDailyStudyRemindersPermissionResult(val isGranted: Boolean) : Action

        sealed interface ViewAction : Action {
            data class ShowTopicCompletedModal(val modalText: String, val isNextStepAvailable: Boolean) : ViewAction

            data class ShowProblemOfDaySolvedModal(val earnedGemsText: String) : ViewAction

            object RequestDailyStudyRemindersPermission : ViewAction

            data class ShowStartPracticingError(val message: String) : ViewAction

            data class ReloadStep(val stepRoute: StepRoute) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
                object HomeScreen : NavigateTo
            }
        }
    }
}