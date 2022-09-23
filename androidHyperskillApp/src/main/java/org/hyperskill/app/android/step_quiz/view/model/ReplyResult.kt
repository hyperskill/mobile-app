package org.hyperskill.app.android.step_quiz.view.model

import org.hyperskill.app.step_quiz.domain.model.submissions.Reply

data class ReplyResult(
    val reply: Reply,
    val validation: Validation
) {
    sealed class Validation {
        object Success : Validation()
        data class Error(val message: String) : Validation()
    }
}