package org.hyperskill.app.step_quiz.presentation

object StepQuizResolver {
    fun isQuizEnabled(state: StepQuizFeature.State.AttemptLoaded): Boolean =
        state.submissionState is StepQuizFeature.SubmissionState.Empty ||
            state.submissionState is StepQuizFeature.SubmissionState.Loaded &&
            state.submissionState.submission.isSubmissionEditable
}