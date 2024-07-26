package org.hyperskill.app.step_quiz.view.model

sealed interface StepQuizFeedbackState {
    data object Idle : StepQuizFeedbackState

    data class Correct(
        val hint: String?,
        val useLatex: Boolean
    ) : StepQuizFeedbackState

    data class Wrong(
        val title: String,
        val description: String?,
        val action: String?,
        val feedbackHint: String?,
        val useFeedbackHintLatex: Boolean,
    ) : StepQuizFeedbackState

    data class RejectedSubmission(val message: String) : StepQuizFeedbackState

    data object Evaluation : StepQuizFeedbackState

    data class ValidationFailed(val message: String) : StepQuizFeedbackState

    data object UnsupportedStep : StepQuizFeedbackState
}