package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult

interface StepQuizFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class AttemptLoading(val oldState: AttemptLoaded) : State
        data class AttemptLoaded(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val currentProfile: Profile
        ) : State

        object NetworkError : State
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
            val currentProfile: Profile
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
            val currentProfile: Profile
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

        object ContinueClicked : Message

        /**
         * Request user permission
         */
        data class RequestUserPermission(val userPermissionRequest: StepQuizUserPermissionRequest) : Message
        data class RequestUserPermissionResult(
            val userPermissionRequest: StepQuizUserPermissionRequest,
            val isGranted: Boolean
        ) : Message

        /**
         * Show problem of day solve modal
         */

        data class ShowProblemOfDaySolvedModal(val earnedGemsText: String) : Message

        object ProblemOfDaySolvedModalGoBackClicked : Message

        /**
         * Analytic
         */
        object ClickedCodeDetailsEventMessage : Message
        object ClickedRetryEventMessage : Message
        object DailyStepCompletedModalShownEventMessage : Message
        object DailyStepCompletedModalHiddenEventMessage : Message
    }

    sealed interface Action {
        data class FetchAttempt(val step: Step) : Action

        data class CreateAttempt(
            val step: Step,
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val shouldResetReply: Boolean
        ) : Action
        data class CreateSubmissionValidateReply(val step: Step, val reply: Reply) : Action
        data class CreateSubmission(val step: Step, val attemptId: Long, val submission: Submission) : Action

        data class RequestUserPermissionResult(
            val userPermissionRequest: StepQuizUserPermissionRequest,
            val isGranted: Boolean
        ) : Action

        /**
         * Analytic
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction // error

            data class RequestUserPermission(val userPermissionRequest: StepQuizUserPermissionRequest) : ViewAction

            data class ShowProblemOfDaySolvedModal(val earnedGemsText: String) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
            }
        }
    }
}