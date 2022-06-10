package org.hyperskill.app.android.step_quiz.presentation.mapper

import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

class StepQuizFeedbackMapper {
    fun mapToStepQuizFeedbackState(stepBlockName: String?, state: StepQuizFeature.State): StepQuizFeedbackState =
        if (state is StepQuizFeature.State.AttemptLoaded && state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
            val castedState = state.submissionState as StepQuizFeature.SubmissionState.Loaded
            when (castedState.submission.status) {
                SubmissionStatus.CORRECT ->
                    StepQuizFeedbackState.Correct(formatHint(stepBlockName, castedState.submission))

                SubmissionStatus.WRONG ->
                    StepQuizFeedbackState.Wrong(formatHint(stepBlockName, castedState.submission))

                SubmissionStatus.EVALUATION ->
                    StepQuizFeedbackState.Evaluation

                else ->
                    StepQuizFeedbackState.Idle
            }
        } else {
            StepQuizFeedbackState.Idle
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
//            ?.let(TextUtil::linkify)
}