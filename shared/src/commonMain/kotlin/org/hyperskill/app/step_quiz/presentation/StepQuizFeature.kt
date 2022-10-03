package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult

interface StepQuizFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object AttemptLoading : State
        data class AttemptLoaded(
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
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val currentProfile: Profile
        ) : Message
        object FetchAttemptError : Message

        data class CreateAttemptClicked(val step: Step) : Message
        data class CreateAttemptSuccess(
            val attempt: Attempt,
            val submissionState: SubmissionState,
            val currentProfile: Profile
        ) : Message
        object CreateAttemptError : Message

        data class CreateSubmissionClicked(val step: Step, val reply: Reply) : Message
        data class CreateSubmissionReplyValidationResult(
            val step: Step,
            val reply: Reply,
            val replyValidation: ReplyValidationResult
        ) : Message
        data class CreateSubmissionSuccess(val submission: Submission) : Message
        object CreateSubmissionNetworkError : Message

        object ContinueClicked : Message

        data class SyncReply(val reply: Reply) : Message

        object NeedToAskUserToEnableDailyReminders : Message
        object UserAgreedToEnableDailyReminders : Message
        object UserDeclinedToEnableDailyReminders : Message

        /**
         * Analytic
         */
        data class ViewedEventMessage(val stepId: Long) : Message
        object ClickedCodeDetailsEventMessage : Message
        object ClickedRetryEventMessage : Message
    }

    sealed interface Action {
        data class FetchAttempt(val step: Step) : Action

        data class CreateAttempt(val step: Step, val attempt: Attempt, val submissionState: SubmissionState) : Action
        data class CreateSubmissionValidateReply(val step: Step, val reply: Reply) : Action
        data class CreateSubmission(val step: Step, val attemptId: Long, val reply: Reply) : Action

        object NotifyUserAgreedToEnableDailyReminders : Action
        object NotifyUserDeclinedToEnableDailyReminders : Action

        /**
         * Analytic
         */
        data class LogViewedEvent(val stepId: Long) : Action
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction // error
            object AskUserToEnableDailyReminders : ViewAction
            sealed interface NavigateTo : ViewAction {
                object HomeScreen : NavigateTo
            }
        }
    }
}