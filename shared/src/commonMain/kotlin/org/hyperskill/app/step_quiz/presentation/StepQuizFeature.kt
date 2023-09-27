package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult

interface StepQuizFeature {
    data class State(
        val stepQuizState: StepQuizState
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

    sealed interface Message {
        data class InitWithStep(val step: Step, val forceUpdate: Boolean = false) : Message
        data class FetchAttemptSuccess(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val isProblemsLimitReached: Boolean,
            val problemsLimitReachedModalText: String?,
            val isParsonsOnboardingShown: Boolean
        ) : Message
        data class FetchAttemptError(val throwable: Throwable) : Message

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
         * Daily limit reached modal
         */
        object ProblemsLimitReachedModalGoToHomeScreenClicked : Message

        /**
         * Parsons problem onboarding modal
         */
        object ParsonsProblemOnboardingModalShownMessage : Message

        /**
         * Click on step theory topic in toolbar
         *
         * @see StepQuizFeature.Action.ViewAction.NavigateTo.StepScreen
         */
        object TheoryToolbarItemClicked : Message

        /**
         * Analytic
         */
        object ClickedCodeDetailsEventMessage : Message
        object FullScreenCodeEditorClickedCodeDetailsEventMessage : Message

        object ClickedStepTextDetailsEventMessage : Message
        object FullScreenCodeEditorClickedStepTextDetailsEventMessage : Message

        object ClickedOpenFullScreenCodeEditorEventMessage : Message

        object ClickedRetryEventMessage : Message

        object ProblemsLimitReachedModalShownEventMessage : Message
        object ProblemsLimitReachedModalHiddenEventMessage : Message

        object ParsonsProblemOnboardingModalHiddenEventMessage : Message
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

        object SaveParsonsProblemOnboardingModalShownCacheFlag : Action

        /**
         * Analytic
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction // error

            object RequestResetCode : ViewAction

            data class ShowProblemsLimitReachedModal(val modalText: String) : ViewAction

            object ShowParsonsProblemOnboardingModal : ViewAction

            sealed interface NavigateTo : ViewAction {
                object Home : NavigateTo

                data class StepScreen(val stepRoute: StepRoute) : NavigateTo
            }
        }
    }
}