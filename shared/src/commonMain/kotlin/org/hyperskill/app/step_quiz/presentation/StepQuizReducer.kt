package org.hyperskill.app.step_quiz.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepQuizReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitWithStep ->
                if (state is State.Idle ||
                    state is State.NetworkError && message.forceUpdate
                ) {
                    State.Loading to setOf(Action.FetchAttempt(message.step))
                } else {
                    null
                }
            is Message.FetchAttemptSuccess ->
                if (state is State.Loading) {
                    State.AttemptLoaded(message.attempt, message.submissionState) to emptySet()
                } else {
                    null
                }
            is Message.FetchAttemptError ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.CreateAttemptClicked ->
                if (state is State.AttemptLoaded) {
                    State.AttemptLoading to setOf(Action.CreateAttempt(message.step, state.attempt, state.submissionState))
                } else {
                    null
                }
            is Message.CreateAttemptSuccess ->
                if (state is State.AttemptLoading) {
                    State.AttemptLoaded(message.attempt, message.submissionState) to emptySet()
                } else {
                    null
                }
            is Message.CreateAttemptError ->
                if (state is State.AttemptLoading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.CreateSubmissionClicked ->
                if (state is State.AttemptLoaded) {
                    val submission = Submission(attempt = state.attempt.id, reply = message.reply, status = SubmissionStatus.EVALUATION)

                    state.copy(submissionState = StepQuizFeature.SubmissionState.Loaded(submission)) to
                        setOf(Action.CreateSubmission(message.step, state.attempt.id, message.reply))
                } else {
                    null
                }
            is Message.CreateSubmissionSuccess ->
                if (state is State.AttemptLoaded) {
                    val newState = state.copy(
                        submissionState = StepQuizFeature.SubmissionState.Loaded(message.submission)
                    )

                    val actions =
                        if ((newState.submissionState as StepQuizFeature.SubmissionState.Loaded).submission.status == SubmissionStatus.CORRECT) {
                            setOf(Action.NotifyStepSolved(state.attempt.step))
                        } else {
                            emptySet()
                        }

                    newState to actions
                } else {
                    null
                }
            is Message.CreateSubmissionError ->
                if (state is State.AttemptLoaded && state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
                    val submission = state.submissionState.submission.copy(status = SubmissionStatus.LOCAL)

                    state.copy(submissionState = StepQuizFeature.SubmissionState.Loaded(submission)) to setOf(Action.ViewAction.ShowNetworkError)
                } else {
                    null
                }
            is Message.SyncReply ->
                if (state is State.AttemptLoaded && StepQuizResolver.isQuizEnabled(state)) {
                    val submission = createLocalSubmission(state, message.reply)
                    state.copy(submissionState = StepQuizFeature.SubmissionState.Loaded(submission)) to emptySet()
                } else {
                    null
                }
        } ?: (state to emptySet())

    private fun createLocalSubmission(oldState: State.AttemptLoaded, reply: Reply): Submission {
        val submissionId = (oldState.submissionState as? StepQuizFeature.SubmissionState.Loaded)
            ?.submission
            ?.id
            ?: 0

        return Submission(id = submissionId, attempt = oldState.attempt.id, reply = reply, status = SubmissionStatus.LOCAL, time = Clock.System.now().toString())
    }
}