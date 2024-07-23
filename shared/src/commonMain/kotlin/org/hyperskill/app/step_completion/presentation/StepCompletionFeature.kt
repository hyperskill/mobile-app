package org.hyperskill.app.step_completion.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams
import org.hyperskill.app.topics.domain.model.Topic

object StepCompletionFeature {
    fun createState(step: Step, stepRoute: StepRoute): State =
        State(
            currentStep = step,
            startPracticingButtonAction = when (stepRoute) {
                is StepRoute.Learn.TheoryOpenedFromPractice,
                is StepRoute.Learn.TheoryOpenedFromSearch,
                is StepRoute.Repeat.Theory ->
                    StartPracticingButtonAction.NavigateToBack
                is StepRoute.Learn.Step,
                is StepRoute.LearnDaily,
                is StepRoute.Repeat.Practice,
                is StepRoute.StageImplement ->
                    StartPracticingButtonAction.FetchNextStepQuiz
            },
            continueButtonAction = when (stepRoute) {
                is StepRoute.Learn -> ContinueButtonAction.CheckTopicCompletion
                else -> ContinueButtonAction.NavigateToBack
            }
        )

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
    @Serializable
    sealed interface ShareStreakData {
        @Serializable
        object Empty : ShareStreakData

        @Serializable
        data class Content(val streakText: String, val streak: Int) : ShareStreakData
    }

    sealed interface Message {
        object StartPracticingClicked : Message

        object ContinuePracticingClicked : Message

        /**
         * Topic completed modal
         */
        sealed interface CheckTopicCompletionStatus : Message {
            data class Completed(
                val topic: Topic,
                val passedTopicsCount: Int,
                val nextLearningActivity: LearningActivity?,
                val isTopicsLimitReached: Boolean
            ) : CheckTopicCompletionStatus

            object Uncompleted : CheckTopicCompletionStatus
            data class Error(val errorMessage: String) : CheckTopicCompletionStatus
        }

        data object TopicCompletedModalGoToStudyPlanClicked : Message
        data object TopicCompletedModalContinueNextTopicClicked : Message
        data class TopicCompletedModalPaywallClicked(val paywallTransitionSource: PaywallTransitionSource) : Message

        /**
         * Show problem of day solve modal
         */
        data class ProblemOfDaySolved(
            val earnedGemsText: String?,
            val shareStreakData: ShareStreakData
        ) : Message

        object ProblemOfDaySolvedModalGoBackClicked : Message
        data class ProblemOfDaySolvedModalShareStreakClicked(val streak: Int) : Message

        /**
         * Share streak
         */
        data class ShareStreak(val streak: Int) : Message
        data class ShareStreakModalShareClicked(val streak: Int) : Message
        data class ShareStreakModalShownEventMessage(val streak: Int) : Message
        data class ShareStreakModalHiddenEventMessage(val streak: Int) : Message
        data class ShareStreakModalNoThanksClickedEventMessage(val streak: Int) : Message

        /**
         * Ask user to rate or review the app
         */
        object RequestUserReview : Message

        /**
         * Analytic
         */
        object DailyStepCompletedModalShownEventMessage : Message
        object DailyStepCompletedModalHiddenEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        data class FetchNextRecommendedStepError(val errorMessage: String) : InternalMessage
        data class FetchNextRecommendedStepSuccess(val newStepRoute: StepRoute) : InternalMessage

        data class StepSolved(val stepId: Long) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class ShowTopicCompletedModal(
                val params: TopicCompletedModalFeatureParams
            ) : ViewAction

            data class ShowProblemOfDaySolvedModal(
                val earnedGemsText: String?,
                val shareStreakData: ShareStreakData
            ) : ViewAction

            data class ShowShareStreakModal(val streak: Int) : ViewAction
            data class ShowShareStreakSystemModal(val streak: Int) : ViewAction

            data class ShowRequestUserReviewModal(val stepRoute: StepRoute) : ViewAction

            data class ShowStartPracticingError(val message: String) : ViewAction

            data class ReloadStep(val stepRoute: StepRoute) : ViewAction

            sealed interface NavigateTo : ViewAction {
                data object Back : NavigateTo
                data object StudyPlan : NavigateTo
                data class Paywall(val paywallTransitionSource: PaywallTransitionSource) : NavigateTo
            }

            sealed interface HapticFeedback : ViewAction {
                object TopicCompleted : HapticFeedback
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchNextRecommendedStep(val currentStep: Step) : InternalAction

        data class CheckTopicCompletionStatus(val topicId: Long) : InternalAction

        data class UpdateProblemsLimit(val chargeStrategy: FreemiumChargeLimitsStrategy) : InternalAction

        object UpdateLastTimeShareStreakShown : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
        data class LogTopicCompletedAnalyticEvent(val topicId: Long) : InternalAction
    }
}