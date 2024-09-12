package org.hyperskill.app.step_quiz.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.code.domain.model.CodeExecutionResult
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.areCommentsAvailable
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.step_quiz.view.model.StepQuizFeedbackState.Wrong.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.submissions.domain.model.formattedText

class StepQuizFeedbackMapper(private val resourcesProvider: ResourceProvider) {

    companion object {
        private const val SEE_HINT_SUGGESTION_THRESHOLD = 1
        private const val SKIP_SUGGESTION_THRESHOLD = 2
    }

    fun map(state: StepQuizFeature.State): StepQuizFeedbackState =
        when (val stepQuizState = state.stepQuizState) {
            is StepQuizFeature.StepQuizState.AttemptLoaded -> {
                val submissionState = stepQuizState.submissionState as? StepQuizFeature.SubmissionState.Loaded
                when (val replyValidation = submissionState?.replyValidation) {
                    is ReplyValidationResult.Error -> StepQuizFeedbackState.ValidationFailed(replyValidation.message)
                    else -> mapSubmission(state, stepQuizState, submissionState)
                }
            }
            is StepQuizFeature.StepQuizState.Unsupported -> StepQuizFeedbackState.UnsupportedStep
            else -> StepQuizFeedbackState.Idle
        }

    private fun mapSubmission(
        state: StepQuizFeature.State,
        stepQuizState: StepQuizFeature.StepQuizState.AttemptLoaded,
        submissionState: StepQuizFeature.SubmissionState.Loaded?
    ): StepQuizFeedbackState {
        val submission = submissionState?.submission
        return when (submission?.status) {
            SubmissionStatus.CORRECT ->
                StepQuizFeedbackState.Correct(
                    hint = getHint(stepQuizState.step, submission, stepQuizState.codeExecutionResult)
                )
            SubmissionStatus.EVALUATION -> StepQuizFeedbackState.Evaluation(
                if (isCodeExecutionLaunched(stepQuizState.step)) {
                    StepQuizFeedbackState.Hint.FromCodeExecution.Loading
                } else {
                    null
                }
            )
            SubmissionStatus.WRONG -> getWrongStatusFeedback(state, stepQuizState, submission)
            SubmissionStatus.REJECTED -> {
                val feedback = submission.feedback
                if (feedback != null) {
                    StepQuizFeedbackState.RejectedSubmission(feedback.formattedText())
                } else {
                    StepQuizFeedbackState.Idle
                }
            }
            else -> StepQuizFeedbackState.Idle
        }
    }

    private fun getWrongStatusFeedback(
        state: StepQuizFeature.State,
        stepQuizState: StepQuizFeature.StepQuizState.AttemptLoaded,
        submission: Submission
    ): StepQuizFeedbackState.Wrong {
        val wrongSubmissionAction = getWrongSubmissionAction(state, stepQuizState)
        return StepQuizFeedbackState.Wrong(
            title = resourcesProvider.getString(SharedResources.strings.step_quiz_status_wrong_title),
            description = when (wrongSubmissionAction) {
                Action.SEE_HINT ->
                    resourcesProvider.getString(SharedResources.strings.step_quiz_status_see_hint_suggestion)
                Action.READ_COMMENTS ->
                    resourcesProvider.getString(SharedResources.strings.step_quiz_status_see_comments_suggestion)
                Action.SKIP_PROBLEM ->
                    resourcesProvider.getString(SharedResources.strings.step_quiz_status_skip_suggestion)
                null -> null
            },
            actionText = when (wrongSubmissionAction) {
                Action.SEE_HINT ->
                    resourcesProvider.getString(SharedResources.strings.step_quiz_status_hint_action)
                Action.READ_COMMENTS ->
                    resourcesProvider.getString(SharedResources.strings.step_quiz_status_comments_action)
                Action.SKIP_PROBLEM ->
                    resourcesProvider.getString(SharedResources.strings.step_quiz_status_skip_action)
                null -> null
            },
            actionType = wrongSubmissionAction,
            hint = getHint(stepQuizState.step, submission, stepQuizState.codeExecutionResult)
        )
    }

    private fun getWrongSubmissionAction(
        state: StepQuizFeature.State,
        stepQuizState: StepQuizFeature.StepQuizState.AttemptLoaded
    ): Action? =
        when (stepQuizState.wrongSubmissionsCount) {
            in 0 until SEE_HINT_SUGGESTION_THRESHOLD -> null
            in SEE_HINT_SUGGESTION_THRESHOLD until SKIP_SUGGESTION_THRESHOLD -> {
                val hintState = state.stepQuizHintsState as? StepQuizHintsFeature.State.Content
                val canSeeHint = hintState?.currentHint == null && hintState?.hintsIds?.isNotEmpty() == true
                when {
                    canSeeHint -> Action.SEE_HINT
                    stepQuizState.step.areCommentsAvailable -> Action.READ_COMMENTS
                    else -> null
                }
            }
            else -> Action.SKIP_PROBLEM
        }

    private fun getHint(
        step: Step,
        submission: Submission,
        codeExecutionResult: CodeExecutionResult?
    ): StepQuizFeedbackState.Hint? =
        when (submission.status) {
            SubmissionStatus.CORRECT -> {
                if (isCodeExecutionLaunched(step) && codeExecutionResult != null) {
                    getCodeExecutionFeedback(codeExecutionResult)
                } else {
                    null
                }
            }
            else -> {
                val text = submission
                    .hint
                    ?.takeIf(String::isNotEmpty)
                    ?.replace("\n", "<br />")
                text?.let {
                    StepQuizFeedbackState.Hint.FromSubmission(
                        text = text,
                        useLatex = step.block.name == BlockName.MATH
                    )
                }
            }
        }

    private fun isCodeExecutionLaunched(step: Step): Boolean =
        step.block.name == BlockName.CODE

    private fun getCodeExecutionFeedback(
        codeExecutionResult: CodeExecutionResult?
    ): StepQuizFeedbackState.Hint.FromCodeExecution.Result? =
        when {
            codeExecutionResult != null -> {
                val stdout = codeExecutionResult.stdout
                if (!stdout.isNullOrEmpty()) {
                    StepQuizFeedbackState.Hint.FromCodeExecution.Result(
                        input = codeExecutionResult.stdin?.takeIf { it.isNotEmpty() }?.trim(),
                        output = codeExecutionResult.stdout.trim()
                    )
                } else {
                    null
                }
            }
            else -> null
        }
}