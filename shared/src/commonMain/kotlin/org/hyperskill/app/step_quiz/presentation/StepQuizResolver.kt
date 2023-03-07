package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.isIdeRequired
import org.hyperskill.app.step.domain.model.supportedBlocksNames
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
                return if (state.step.block.name == BlockName.SQL) {
                    true
                } else {
                    !isNeedRecreateAttemptForNewSubmission(state.step)
                }
            }
        }

        return false
    }

    fun isQuizLoading(state: StepQuizFeature.State): Boolean =
        when (state) {
            is StepQuizFeature.State.AttemptLoaded -> {
                when (state.submissionState) {
                    is StepQuizFeature.SubmissionState.Empty -> false
                    is StepQuizFeature.SubmissionState.Loaded ->
                        state.submissionState.submission.status == SubmissionStatus.EVALUATION
                }
            }
            is StepQuizFeature.State.AttemptLoading -> true
            StepQuizFeature.State.Idle -> false
            StepQuizFeature.State.Loading -> true
            StepQuizFeature.State.NetworkError -> false
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
            BlockName.PYCHARM,
            BlockName.MATH ->
                true
            else ->
                false
        }

    fun isQuizSupportable(step: Step): Boolean =
        BlockName.supportedBlocksNames.contains(step.block.name) && !step.isIdeRequired()
}