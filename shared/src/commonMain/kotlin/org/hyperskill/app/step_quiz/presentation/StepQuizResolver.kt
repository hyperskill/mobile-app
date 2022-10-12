package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus

object StepQuizResolver {
    fun isQuizEnabled(state: StepQuizFeature.State.AttemptLoaded): Boolean {
        if (state.submissionState is StepQuizFeature.SubmissionState.Empty) {
            return true
        }

        if (state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
            if (state.submissionState.submission.status == SubmissionStatus.LOCAL) {
                return true
            }
            if (state.submissionState.submission.status == SubmissionStatus.WRONG) {
                return !isNeedRecreateAttemptForNewSubmission(state.step)
            }
        }

        return false
    }

    fun isNeedRecreateAttemptForNewSubmission(step: Step): Boolean =
        when (step.block.name) {
            BlockName.CHOICE,
            BlockName.SQL,
            BlockName.TABLE ->
                true
            else ->
                false
        }

    fun isQuizRetriable(step: Step): Boolean =
        when (step.block.name) {
            BlockName.CODE,
            BlockName.SQL,
            BlockName.MATH ->
                true
            else ->
                false
        }
}