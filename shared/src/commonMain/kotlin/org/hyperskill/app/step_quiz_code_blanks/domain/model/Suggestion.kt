package org.hyperskill.app.step_quiz_code_blanks.domain.model

sealed class Suggestion {
    abstract val text: String

    data object Print : Suggestion() {
        override val text: String
            get() = "print"
    }

    data class ConstantString(override val text: String) : Suggestion()
}