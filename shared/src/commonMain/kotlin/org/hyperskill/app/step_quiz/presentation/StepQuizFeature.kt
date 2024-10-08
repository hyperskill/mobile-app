package org.hyperskill.app.step_quiz.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.run_code.domain.model.RunCodeExecutionResult
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription

object StepQuizFeature {
    data class State(
        val stepQuizState: StepQuizState,
        val stepQuizHintsState: StepQuizHintsFeature.State,
        val stepQuizToolbarState: StepQuizToolbarFeature.State,
        val stepQuizCodeBlanksState: StepQuizCodeBlanksFeature.State
    )

    sealed interface StepQuizState {
        data object Idle : StepQuizState
        data object Loading : StepQuizState
        data object Unsupported : StepQuizState
        data class AttemptLoading(val oldState: AttemptLoaded) : StepQuizState
        data class AttemptLoaded(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val isProblemsLimitReached: Boolean,
            internal val isTheoryAvailable: Boolean,
            internal val wrongSubmissionsCount: Int = 0,
            internal val runCodeExecutionResult: RunCodeExecutionResult? = null
        ) : StepQuizState

        data object NetworkError : StepQuizState
    }
    sealed interface SubmissionState {
        data class Empty(val reply: Reply? = null) : SubmissionState
        data class Loaded(
            val submission: Submission,
            val replyValidation: ReplyValidationResult? = null
        ) : SubmissionState
    }

    @Serializable
    sealed interface ProblemOnboardingModal {
        @Serializable
        data object Parsons : ProblemOnboardingModal
    }

    internal sealed interface ChildFeatureMessage

    sealed interface Message {
        data class InitWithStep(val step: Step, val forceUpdate: Boolean = false) : Message

        /**
         * Create/retry attempt
         */
        data class CreateAttemptClicked(val step: Step, val shouldResetReply: Boolean) : Message
        data class CreateAttemptSuccess(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val isProblemsLimitReached: Boolean
        ) : Message
        data object CreateAttemptError : Message

        /**
         * Submit submission
         */
        data class CreateSubmissionClicked(val step: Step, val reply: Reply) : Message
        data class CreateSubmissionSuccess(
            val submission: Submission,
            val newAttempt: Attempt? = null,
            val runCodeExecutionResult: RunCodeExecutionResult? = null
        ) : Message
        data object CreateSubmissionNetworkError : Message
        data class CreateSubmissionReplyValidationResult(
            val step: Step,
            val reply: Reply,
            val replyValidation: ReplyValidationResult
        ) : Message

        data class SyncReply(val reply: Reply) : Message

        /**
         * Reset code
         */
        data class RequestResetCodeResult(val isGranted: Boolean) : Message

        /**
         * Problem onboarding modal
         */
        data class ProblemOnboardingModalShownMessage(val modalType: ProblemOnboardingModal) : Message
        data class ProblemOnboardingModalHiddenMessage(val modalType: ProblemOnboardingModal) : Message

        /**
         * Click on step theory topic in toolbar
         *
         * @see StepQuizFeature.Action.ViewAction.NavigateTo.TheoryStepScreen
         */
        data object TheoryToolbarItemClicked : Message

        data object SeeHintClicked : Message
        data object ReadCommentsClicked : Message
        data object SkipClicked : Message

        data object UnsupportedQuizSolveOnTheWebClicked : Message
        data object UnsupportedQuizGoToStudyPlanClicked : Message

        /**
         * Click on child quiz UI when it's disabled
         */
        data object ChildQuizClickedWhenDisabled : Message

        /**
         * Analytic
         */
        data object ClickedCodeDetailsEventMessage : Message
        data object FullScreenCodeEditorClickedCodeDetailsEventMessage : Message

        data object ClickedStepTextDetailsEventMessage : Message
        data object FullScreenCodeEditorClickedStepTextDetailsEventMessage : Message

        data object ClickedOpenFullScreenCodeEditorEventMessage : Message

        data class CodeEditorClickedInputAccessoryButtonEventMessage(val symbol: String) : Message

        data object ClickedRetryEventMessage : Message

        /**
         * Message Wrappers
         */
        data class StepQuizHintsMessage(val message: StepQuizHintsFeature.Message) : Message, ChildFeatureMessage
        data class StepQuizToolbarMessage(val message: StepQuizToolbarFeature.Message) : Message, ChildFeatureMessage
        data class StepQuizCodeBlanksMessage(
            val message: StepQuizCodeBlanksFeature.Message
        ) : Message, ChildFeatureMessage
    }

    internal sealed interface InternalMessage : Message {
        data object FetchAttemptError : InternalMessage
        data class FetchAttemptSuccess(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val subscription: Subscription,
            val isProblemsLimitReached: Boolean,
            val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
            val problemsOnboardingFlags: ProblemsOnboardingFlags
        ) : InternalMessage

        data class UpdateProblemsLimitResult(
            val subscription: Subscription,
            val isProblemsLimitReached: Boolean,
            val chargeLimitsStrategy: FreemiumChargeLimitsStrategy
        ) : InternalMessage

        data class ProblemsLimitChanged(
            val subscription: Subscription,
            val isProblemsLimitReached: Boolean
        ) : InternalMessage

        data object CreateMagicLinkForUnsupportedQuizError : InternalMessage
        data class CreateMagicLinkForUnsupportedQuizSuccess(val url: String) : InternalMessage
    }

    sealed interface Action {
        data class FetchAttempt(val step: Step) : Action

        data class CreateAttempt(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val isProblemsLimitReached: Boolean,
            val shouldResetReply: Boolean
        ) : Action

        data class CreateSubmissionValidateReply(
            val step: Step,
            val dataset: Dataset?,
            val reply: Reply
        ) : Action
        data class CreateSubmission(
            val step: Step,
            val stepContext: StepContext,
            val attemptId: Long,
            val submission: Submission
        ) : Action

        data class SaveProblemOnboardingModalShownCacheFlag(val modalType: ProblemOnboardingModal) : Action

        /**
         * Action Wrappers
         */
        data class StepQuizHintsAction(val action: StepQuizHintsFeature.Action) : Action
        data class StepQuizToolbarAction(val action: StepQuizToolbarFeature.Action) : Action
        data class StepQuizCodeBlanksAction(val action: StepQuizCodeBlanksFeature.Action) : Action

        sealed interface ViewAction : Action {
            data object ShowNetworkError : ViewAction // error

            data object RequestResetCode : ViewAction

            data class ShowProblemsLimitReachedModal(
                val subscription: Subscription,
                val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
                val context: ProblemsLimitInfoModalContext
            ) : ViewAction

            data object HideProblemsLimitReachedModal : ViewAction

            data class ShowProblemOnboardingModal(val modalType: ProblemOnboardingModal) : ViewAction

            data class StepQuizHintsViewAction(
                val viewAction: StepQuizHintsFeature.Action.ViewAction
            ) : ViewAction

            data class StepQuizToolbarViewAction(
                val viewAction: StepQuizToolbarFeature.Action.ViewAction
            ) : ViewAction

            data class StepQuizCodeBlanksViewAction(
                val viewAction: StepQuizCodeBlanksFeature.Action.ViewAction
            ) : ViewAction

            sealed interface CreateMagicLinkState : ViewAction {
                data object Loading : CreateMagicLinkState
                data object Error : CreateMagicLinkState
                data object Success : CreateMagicLinkState
            }
            data class OpenUrl(val url: String) : ViewAction

            data object RequestShowComments : ViewAction
            data object RequestSkipStep : ViewAction

            /**
             * Code blanks requests to scroll to call to action button and highlight it
             *
             * @see StepQuizChildFeatureReducer.reduceStepQuizCodeBlanksMessage
             */
            data object HighlightCallToActionButton : ViewAction
            data object UnhighlightCallToActionButton : ViewAction

            data object BounceCallToActionButton : ViewAction

            sealed interface ScrollTo : ViewAction {
                data object Hints : ScrollTo
                data object CallToActionButton : ScrollTo
            }

            sealed interface NavigateTo : ViewAction {
                data object StudyPlan : NavigateTo
                data class TheoryStepScreen(val stepRoute: StepRoute) : NavigateTo
            }

            sealed interface HapticFeedback : ViewAction {
                data object ReplyValidationError : HapticFeedback

                data object WrongSubmission : HapticFeedback
                data object CorrectSubmission : HapticFeedback
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class UpdateProblemsLimit(val chargeStrategy: FreemiumChargeLimitsStrategy) : InternalAction

        data class CreateMagicLinkForUnsupportedQuiz(val stepRoute: StepRoute) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}