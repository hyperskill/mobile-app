package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

interface StepQuizFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object AttemptLoading : State
        data class AttemptLoaded(
            val attempt: Attempt,
            val submissionState: SubmissionState
        ) : State

        object NetworkError : State
    }

    sealed interface SubmissionState {
        data class Empty(val reply: Reply? = null) : SubmissionState
        data class Loaded(val submission: Submission) : SubmissionState
    }

    sealed interface Message {
        data class InitWithStep(val step: Step, val forceUpdate: Boolean = false) : Message
        data class FetchAttemptSuccess(
            val attempt: Attempt,
            val submissionState: SubmissionState
        ) : Message
        object FetchAttemptError : Message

        data class CreateAttemptClicked(val step: Step) : Message
        data class CreateAttemptSuccess(val attempt: Attempt, val submissionState: SubmissionState) : Message
        object CreateAttemptError : Message

        data class CreateSubmissionClicked(val step: Step, val reply: Reply) : Message
        data class CreateSubmissionSuccess(val submission: Submission) : Message
        object CreateSubmissionError : Message

        data class SyncReply(val reply: Reply) : Message
    }

    sealed interface Action {
        data class FetchAttempt(val step: Step) : Action

        data class CreateAttempt(val step: Step, val attempt: Attempt, val submissionState: SubmissionState) : Action
        data class CreateSubmission(val step: Step, val attemptId: Long, val reply: Reply) : Action

        sealed interface ViewAction : Action {
            object ShowNetworkError : ViewAction // error
        }
    }
}