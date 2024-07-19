package org.hyperskill.app.step_quiz_code_blanks.domain.model

sealed class CodeBlock {
    abstract val isActive: Boolean

    abstract val suggestions: List<Suggestion>

    internal abstract val analyticRepresentation: String

    data class Blank(
        override val isActive: Boolean
    ) : CodeBlock() {
        override val suggestions: List<Suggestion>
            get() = listOf(Suggestion.Print)

        override fun toString(): String = ""

        override val analyticRepresentation: String
            get() = "Blank(isActive=$isActive, suggestions=$suggestions)"
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
                    append(selectedSuggestion.text)
                }
                append(")")
            }

        override val analyticRepresentation: String
            get() = "Print(isActive=$isActive, suggestions=$suggestions, selectedSuggestion=$selectedSuggestion)"
    }
}