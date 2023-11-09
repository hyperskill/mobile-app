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
                    ContinueButtonAction.CheckTopicCompletion
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
        object NavigateToStudyPlan : ContinueButtonAction
        object NavigateToBack : ContinueButtonAction
        object CheckTopicCompletion : ContinueButtonAction
    }

    /**
     * Helper class to show streak text and streak value
     *
     * @see Message.ProblemOfDaySolved
     * @see Action.ViewAction.ShowProblemOfDaySolvedModal
     */
    sealed interface ShareStreakData {
        object Empty : ShareStreakData
        data class Content(val streakText: String, val streak: Int) : ShareStreakData
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
                val topicId: Long,
                val modalText: String,
                val nextLearningActivity: LearningActivity?
            ) : CheckTopicCompletionStatus
            object Uncompleted : CheckTopicCompletionStatus
            data class Error(val errorMessage: String) : CheckTopicCompletionStatus
        }

        object TopicCompletedModalGoToStudyPlanClicked : Message
        object TopicCompletedModalContinueNextTopicClicked : Message

        sealed interface FetchNextRecommendedStepResult : Message {
            data class Success(val newStepRoute: StepRoute) : FetchNextRecommendedStepResult
            data class Error(val errorMessage: String) : FetchNextRecommendedStepResult
        }

        /**
         * Show problem of day solve modal
         */
        data class ProblemOfDaySolved(
            val earnedGemsText: String,
            val shareStreakData: ShareStreakData
        ) : Message
        object ProblemOfDaySolvedModalGoBackClicked : Message
        data class ProblemOfDaySolvedModalShareStreakClicked(val streak: Int) : Message

        /**
         * Daily study reminders
         */
        object RequestDailyStudyRemindersPermission : Message
        data class RequestDailyStudyRemindersPermissionResult(val isGranted: Boolean) : Message

        /**
         * Share streak
         */
        data class ShareStreak(val streak: Int) : Message
        data class ShareStreakModalShownEventMessage(val streak: Int) : Message
        data class ShareStreakModalHiddenEventMessage(val streak: Int) : Message
        data class ShareStreakModalShareClickedEventMessage(val streak: Int) : Message
        data class ShareStreakModalNoThanksClickedEventMessage(val streak: Int) : Message

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
        data class LogTopicCompletedAnalyticEvent(val topicId: Long) : Action

        data class CheckTopicCompletionStatus(val topicId: Long) : Action

        object UpdateProblemsLimit : Action

        object TurnOnDailyStudyReminder : Action
        object PostponeDailyStudyReminder : Action

        object UpdateLastTimeShareStreakShown : Action

        sealed interface ViewAction : Action {
            data class ShowTopicCompletedModal(
                val modalText: String,
                val isNextStepAvailable: Boolean
            ) : ViewAction

            data class ShowProblemOfDaySolvedModal(
                val earnedGemsText: String,
                val shareStreakData: ShareStreakData
            ) : ViewAction

            data class ShowShareStreakModal(val streak: Int) : ViewAction

            object RequestDailyStudyRemindersPermission : ViewAction

            data class ShowStartPracticingError(val message: String) : ViewAction

            data class ReloadStep(val stepRoute: StepRoute) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
                object StudyPlan : NavigateTo
            }
        }
    }
}