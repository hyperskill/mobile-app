package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.domain.model.isIdeRequired
import org.hyperskill.app.step.domain.model.supportedBlocksNames
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.model.submissions.isWrongOrRejected

object StepQuizResolver {
    fun isQuizEnabled(state: StepQuizFeature.StepQuizState.AttemptLoaded): Boolean {
        if (state.isProblemsLimitReached) {
            return false
        }

        if (state.submissionState is StepQuizFeature.SubmissionState.Empty) {
            return true
        }

        if (state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
            if (state.submissionState.submission.status == SubmissionStatus.LOCAL) {
                return true
            }
            if (state.submissionState.submission.status.isWrongOrRejected) {
                return if (state.step.block.name == BlockName.SQL) {
                    true
                } else {
                    !isNeedRecreateAttemptForNewSubmission(state.step)
                }
            }
        }

        return false
    }

    fun isQuizLoading(state: StepQuizFeature.StepQuizState): Boolean =
        when (state) {
            is StepQuizFeature.StepQuizState.AttemptLoaded -> {
                when (state.submissionState) {
                    is StepQuizFeature.SubmissionState.Empty -> false
                    is StepQuizFeature.SubmissionState.Loaded ->
                        state.submissionState.submission.status == SubmissionStatus.EVALUATION
                }
            }
            is StepQuizFeature.StepQuizState.AttemptLoading -> true
            StepQuizFeature.StepQuizState.Idle -> false
            StepQuizFeature.StepQuizState.Loading -> true
            StepQuizFeature.StepQuizState.NetworkError -> false
            StepQuizFeature.StepQuizState.Unsupported -> false
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

    internal fun isIdeRequired(step: Step, submissionState: StepQuizFeature.SubmissionState): Boolean {
        if (step.isIdeRequired()) {
            return true
        } else if (step.block.name == BlockName.PYCHARM) {
            val reply = when (submissionState) {
                is StepQuizFeature.SubmissionState.Empty -> submissionState.reply
                is StepQuizFeature.SubmissionState.Loaded -> submissionState.submission.reply
            } ?: return false

            val visibleFilesCount = reply.solution?.count { it.isVisible } ?: 0

            return visibleFilesCount > 1 || (visibleFilesCount <= 1 && reply.checkProfile?.isEmpty() == true)
        }
        return false
    }

    fun isTheoryToolbarItemAvailable(state: StepQuizFeature.StepQuizState): Boolean =
        when (state) {
            is StepQuizFeature.StepQuizState.AttemptLoaded -> {
                state.isTheoryAvailable
            }
            is StepQuizFeature.StepQuizState.AttemptLoading -> {
                state.oldState.isTheoryAvailable
            }
            StepQuizFeature.StepQuizState.Idle,
            StepQuizFeature.StepQuizState.Loading,
            StepQuizFeature.StepQuizState.NetworkError,
            StepQuizFeature.StepQuizState.Unsupported -> {
                false
            }
        }

    internal fun isTheoryAvailable(stepRoute: StepRoute, step: Step): Boolean =
        when (stepRoute) {
            is StepRoute.Learn.Step,
            is StepRoute.Repeat.Practice -> {
                step.topicTheory != null
            }
            is StepRoute.LearnDaily,
            is StepRoute.StageImplement,
            is StepRoute.Learn.TheoryOpenedFromPractice,
            is StepRoute.Repeat.Theory -> {
                false
            }
        }
}