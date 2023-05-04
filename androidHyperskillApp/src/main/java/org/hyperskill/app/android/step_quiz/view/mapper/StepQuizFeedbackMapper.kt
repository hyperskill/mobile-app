package org.hyperskill.app.android.step_quiz.view.mapper

import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.model.submissions.formattedText
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

class StepQuizFeedbackMapper {
    fun mapToStepQuizFeedbackState(stepBlockName: String?, state: StepQuizFeature.StepQuizState): StepQuizFeedbackState {
        val submissionState = (state as? StepQuizFeature.StepQuizState.AttemptLoaded)?.submissionState
        return if (submissionState is StepQuizFeature.SubmissionState.Loaded) {
            when (submissionState.submission.status)  {
                SubmissionStatus.CORRECT ->
                    StepQuizFeedbackState.Correct(formatHint(stepBlockName, submissionState.submission))

                SubmissionStatus.WRONG ->
                    StepQuizFeedbackState.Wrong(formatHint(stepBlockName, submissionState.submission))

                SubmissionStatus.EVALUATION ->
                    StepQuizFeedbackState.Evaluation

                SubmissionStatus.REJECTED -> {
                    val feedback = submissionState.submission.feedback
                    if (feedback != null) {
                        StepQuizFeedbackState.RejectedSubmission(feedback.formattedText())
                    } else {
                        StepQuizFeedbackState.Idle
                    }
                }

                else -> StepQuizFeedbackState.Idle
            }
        } else {
            StepQuizFeedbackState.Idle
        }
    }

    private fun formatHint(stepBlockName: String?, submission: Submission): String? =
        submission
            .hint
            ?.takeIf(String::isNotEmpty)
            ?.replace("\n", "<br />")
            ?.let {
                val showLaTeX = stepBlockName == "math"
                if (showLaTeX) {
                    it
                } else {
                    """<pre><span style="font-family: 'Roboto';">$it</span></pre>"""
                }
            }
}