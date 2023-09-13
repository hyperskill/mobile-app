package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step_quiz.domain.model.submissions.isWrongOrRejected

/**
 * Uses on iOS to avoid unnecessary UI re-rendering each time on reply change.
 */
fun StepQuizResolver.shouldSyncReply(state: StepQuizFeature.StepQuizState): Boolean {
    when (state) {
        is StepQuizFeature.StepQuizState.AttemptLoaded -> {
            if (!isQuizEnabled(state)) {
                return false
            }

            when (state.submissionState) {
                is StepQuizFeature.SubmissionState.Empty -> {
                    return false
                }
                is StepQuizFeature.SubmissionState.Loaded -> {
                    if (state.submissionState.replyValidation != null) {
                        return true
                    }
                    if (state.submissionState.submission.status.isWrongOrRejected) {
                        return true
                    }
                    return false
                }
            }
        }
        is StepQuizFeature.StepQuizState.AttemptLoading,
        StepQuizFeature.StepQuizState.Idle,
        StepQuizFeature.StepQuizState.Loading,
        StepQuizFeature.StepQuizState.NetworkError,
        StepQuizFeature.StepQuizState.Unsupported -> {
            return false
        }
    }
}
