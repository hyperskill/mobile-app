package org.hyperskill.app.step_quiz_code_blanks.domain.model

sealed class CodeBlock {
    abstract val isActive: Boolean

    abstract val suggestions: List<Suggestion>

    data class Blank(
        override val isActive: Boolean
    ) : CodeBlock() {
        override val suggestions: List<Suggestion>
            get() = listOf(Suggestion.Print)

        override fun toString(): String = ""
    }

    data class Print(
        override val isActive: Boolean,
        override val suggestions: List<Suggestion.ConstantString>,
        val selectedSuggestion: Suggestion.ConstantString?
    ) : CodeBlock() {
        override fun toString(): String =
            buildString {
                append("print(")

                if (selectedSuggestion != null) {
                    append("\"${selectedSuggestion.text}\"")
                }

                append(")")
            }
    }
}