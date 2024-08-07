package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.domain.model.isIdeRequired
import org.hyperskill.app.step.domain.model.isSupported
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.submissions.domain.model.isWrongOrRejected

@Suppress("TooManyFunctions")
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
            BlockName.MATH,
            BlockName.FILL_BLANKS,
            BlockName.PROMPT ->
                true
            else ->
                false
        }

    fun isQuizSupportable(step: Step): Boolean =
        step.isSupported() && !step.isIdeRequired()

    internal fun isIdeRequired(step: Step, submissionState: StepQuizFeature.SubmissionState): Boolean {
        if (step.isIdeRequired()) {
            return true
        } else if (step.block.name == BlockName.PYCHARM) {
            val reply = submissionState.reply ?: return false
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
            is StepRoute.Learn.TheoryOpenedFromSearch,
            is StepRoute.Repeat.Theory -> {
                false
            }
        }

    /**
     * @return Returns `true` if step route has limited number of attempts.
     */
    internal fun isStepHasLimitedAttempts(stepRoute: StepRoute): Boolean =
        when (stepRoute) {
            is StepRoute.Learn.Step,
            is StepRoute.StageImplement -> true
            is StepRoute.Learn.TheoryOpenedFromPractice,
            is StepRoute.Learn.TheoryOpenedFromSearch,
            is StepRoute.LearnDaily,
            is StepRoute.Repeat.Practice,
            is StepRoute.Repeat.Theory -> false
        }

    /**
     * Is used to avoid unnecessary UI re-rendering each time on reply change.
     */
    fun shouldSyncReply(state: StepQuizFeature.StepQuizState): Boolean {
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
}