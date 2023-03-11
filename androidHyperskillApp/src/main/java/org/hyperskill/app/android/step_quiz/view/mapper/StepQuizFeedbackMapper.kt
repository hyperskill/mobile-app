package org.hyperskill.app.android.step_quiz.view.mapper

import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.model.submissions.formattedText
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

class StepQuizFeedbackMapper {
    fun mapToStepQuizFeedbackState(stepBlockName: String?, state: StepQuizFeature.State): StepQuizFeedbackState {
        val submissionState = (state as? StepQuizFeature.State.AttemptLoaded)?.submissionState
        return if (submissionState is StepQuizFeature.SubmissionState.Loaded) {
            when  {
                submissionState.submission.feedback != null ->
                    StepQuizFeedbackState.RejectedSubmission(submissionState.submission.feedback!!.formattedText())

                submissionState.submission.status == SubmissionStatus.CORRECT ->
                    StepQuizFeedbackState.Correct(formatHint(stepBlockName, submissionState.submission))

                submissionState.submission.status == SubmissionStatus.WRONG ->
                    StepQuizFeedbackState.Wrong(formatHint(stepBlockName, submissionState.submission))

                submissionState.submission.status == SubmissionStatus.EVALUATION ->
                    StepQuizFeedbackState.Evaluation

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