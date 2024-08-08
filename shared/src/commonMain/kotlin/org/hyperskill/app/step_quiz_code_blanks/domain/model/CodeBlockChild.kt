package org.hyperskill.app.step_quiz_code_blanks.domain.model

sealed class CodeBlockChild {
    abstract val isActive: Boolean

    abstract val suggestions: List<Suggestion>

    internal abstract val analyticRepresentation: String

    data class SelectSuggestion(
        override val isActive: Boolean,
        override val suggestions: List<Suggestion>,
        val selectedSuggestion: Suggestion.ConstantString?
    ) : CodeBlockChild() {
        override val analyticRepresentation: String =
            "SelectSuggestion(isActive=$isActive, suggestions=$suggestions, selectedSuggestion=$selectedSuggestion)"

        override fun toString(): String =
            selectedSuggestion?.text ?: ""
    }
}