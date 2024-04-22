package org.hyperskill.app.step_quiz.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription

object StepQuizFeature {
    data class State(
        val stepQuizState: StepQuizState,
        val stepQuizHintsState: StepQuizHintsFeature.State,
        val problemsLimitState: ProblemsLimitFeature.State
    )

    sealed interface StepQuizState {
        object Idle : StepQuizState
        object Loading : StepQuizState
        object Unsupported : StepQuizState
        data class AttemptLoading(val oldState: AttemptLoaded) : StepQuizState
        data class AttemptLoaded(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val isProblemsLimitReached: Boolean,
            val isFixGptCodeGenerationMistakesBadgeVisible: Boolean = false,
            internal val isTheoryAvailable: Boolean
        ) : StepQuizState

        object NetworkError : StepQuizState
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
        object Parsons : ProblemOnboardingModal

        @Serializable
        data class FillBlanks(val mode: FillBlanksMode) : ProblemOnboardingModal

        @Serializable
        object GptCodeGenerationWithErrors : ProblemOnboardingModal
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
        object CreateAttemptError : Message

        /**
         * Submit submission
         */
        data class CreateSubmissionClicked(val step: Step, val reply: Reply) : Message
        data class CreateSubmissionSuccess(val submission: Submission, val newAttempt: Attempt? = null) : Message
        object CreateSubmissionNetworkError : Message
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
         * @see StepQuizFeature.Action.ViewAction.NavigateTo.StepScreen
         */
        object TheoryToolbarItemClicked : Message

        object UnsupportedQuizSolveOnTheWebClicked : Message
        object UnsupportedQuizGoToStudyPlanClicked : Message

        /**
         * Analytic
         */
        object ClickedCodeDetailsEventMessage : Message
        object FullScreenCodeEditorClickedCodeDetailsEventMessage : Message

        object ClickedStepTextDetailsEventMessage : Message
        object FullScreenCodeEditorClickedStepTextDetailsEventMessage : Message

        object ClickedOpenFullScreenCodeEditorEventMessage : Message

        data class CodeEditorClickedInputAccessoryButtonEventMessage(val symbol: String) : Message

        object ClickedRetryEventMessage : Message

        object FixGptGeneratedCodeMistakesBadgeClickedQuestionMark : Message

        /**
         * Message Wrappers
         */
        data class StepQuizHintsMessage(val message: StepQuizHintsFeature.Message) : Message, ChildFeatureMessage
        data class ProblemsLimitMessage(val message: ProblemsLimitFeature.Message) : Message, ChildFeatureMessage
    }

    internal sealed interface InternalMessage : Message {
        object FetchAttemptError : InternalMessage
        data class FetchAttemptSuccess(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val subscription: Subscription,
            val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
            val problemsOnboardingFlags: ProblemsOnboardingFlags,
            val isMobileGptCodeGenerationWithErrorsEnabled: Boolean
        ) : InternalMessage

        data class GenerateGptCodeWithErrorsResult(
            val attemptLoadedState: StepQuizState.AttemptLoaded,
            val code: String?
        ) : InternalMessage

        data class UpdateProblemsLimitResult(
            val subscription: Subscription,
            val chargeLimitsStrategy: FreemiumChargeLimitsStrategy
        ) : InternalMessage

        data class ProblemsLimitChanged(val subscription: Subscription) : InternalMessage

        object CreateMagicLinkForUnsupportedQuizError : InternalMessage
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

        data class CreateSubmissionValidateReply(val step: Step, val reply: Reply) : Action
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
        data class ProblemsLimitAction(val action: ProblemsLimitFeature.Action) : Action

        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction // error

            object RequestResetCode : ViewAction

            data class ShowProblemsLimitReachedModal(
                val subscription: Subscription,
                val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
                val context: ProblemsLimitInfoModalContext
            ) : ViewAction

            object HideProblemsLimitReachedModal : ViewAction

            data class ShowProblemOnboardingModal(val modalType: ProblemOnboardingModal) : ViewAction

            data class StepQuizHintsViewAction(
                val viewAction: StepQuizHintsFeature.Action.ViewAction
            ) : ViewAction

            data class ProblemsLimitViewAction(
                val viewAction: ProblemsLimitFeature.Action.ViewAction
            ) : ViewAction

            sealed interface CreateMagicLinkState : ViewAction {
                object Loading : CreateMagicLinkState
                object Error : CreateMagicLinkState
                object Success : CreateMagicLinkState
            }
            data class OpenUrl(val url: String) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object StudyPlan : NavigateTo
                data class StepScreen(val stepRoute: StepRoute) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class GenerateGptCodeWithErrors(val attemptLoadedState: StepQuizState.AttemptLoaded) : InternalAction

        data class UpdateProblemsLimit(val chargeStrategy: FreemiumChargeLimitsStrategy) : InternalAction

        data class CreateMagicLinkForUnsupportedQuiz(val stepRoute: StepRoute) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}