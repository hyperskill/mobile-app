package org.hyperskill.app.step_quiz.domain.validation

sealed interface ReplyValidationResult {
    object Success : ReplyValidationResult
    data class Error(val message: String) : ReplyValidationResult
}