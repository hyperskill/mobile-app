package org.hyperskill.app.step_quiz_code_blanks.domain.model

sealed class Suggestion {
    abstract val text: String

    internal abstract val analyticRepresentation: String

    data object Print : Suggestion() {
        override val text: String
            get() = "print"

        override val analyticRepresentation: String
            get() = "Print(text='$text')"
    }

    data class ConstantString(
        override val text: String
    ) : Suggestion() {
        override val analyticRepresentation: String
            get() = "ConstantString(text='$text')"
    }
}