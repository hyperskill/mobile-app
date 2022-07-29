package org.hyperskill.app.android.step_quiz_code.view.model

sealed class CodeStepQuizFormState {
    object Idle : CodeStepQuizFormState()
    data class Lang(val lang: String, val code: String) : CodeStepQuizFormState()
}