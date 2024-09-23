package org.hyperskill.app.step_quiz.view.model

sealed interface StepQuizFeedbackState {
    data object Idle : StepQuizFeedbackState

    data class Correct(val hint: Hint?) : StepQuizFeedbackState

    data class Wrong(
        val title: String,
        val description: String?,
        val actionText: String?,
        val actionType: Action?,
        val hint: Hint?
    ) : StepQuizFeedbackState {
        enum class Action {
            SEE_HINT,
            READ_COMMENTS,
            SKIP_PROBLEM
        }
    }

    data class RejectedSubmission(val message: String) : StepQuizFeedbackState

    data class Evaluation(val hint: Hint.FromRunCodeExecution?) : StepQuizFeedbackState

    data class ValidationFailed(val message: String) : StepQuizFeedbackState

    data object UnsupportedStep : StepQuizFeedbackState

    sealed interface Hint {
        data class FromSubmission(
            val text: String,
            val useLatex: Boolean
        ) : Hint

        sealed interface FromRunCodeExecution : Hint {
            data object Loading : FromRunCodeExecution
            data class Result(
                val input: String?,
                val output: String
            ) : FromRunCodeExecution
        }
    }
}