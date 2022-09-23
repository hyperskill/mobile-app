package org.hyperskill.app.android.step_quiz.view.model

sealed class StepQuizFeedbackState {
    object Idle : StepQuizFeedbackState()
    data class Correct(val hint: String?) : StepQuizFeedbackState()
    data class Wrong(val hint: String?) : StepQuizFeedbackState()
    object Evaluation : StepQuizFeedbackState()
    data class Validation(val message: String) : StepQuizFeedbackState()
}