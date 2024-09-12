package org.hyperskill.app.step_quiz_code_blanks.domain.model

sealed class Suggestion {
    abstract val text: String

    internal abstract val analyticRepresentation: String

    data object Print : Suggestion() {
        override val text: String = "print"

        override val analyticRepresentation: String =
            "Print(text='$text')"
    }

    data object Variable : Suggestion() {
        override val text: String = "variable"

        override val analyticRepresentation: String =
            "Variable(text='$text')"
    }

    data object IfStatement : Suggestion() {
        override val text: String = "if"

        override val analyticRepresentation: String =
            "IfStatement(text='$text')"
    }

    data class ConstantString(
        override val text: String
    ) : Suggestion() {
        override val analyticRepresentation: String
            get() = "ConstantString(text='$text')"

        internal val isOpeningParentheses: Boolean
            get() = text == "("

        internal val isClosingParentheses: Boolean
            get() = text == ")"
    }
}