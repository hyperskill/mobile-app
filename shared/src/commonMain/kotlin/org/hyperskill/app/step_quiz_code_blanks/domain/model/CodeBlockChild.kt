package org.hyperskill.app.step_quiz_code_blanks.domain.model

sealed class CodeBlockChild {
    internal abstract val isActive: Boolean

    internal abstract val suggestions: List<Suggestion>

    internal abstract fun toReplyString(): String

    internal data class SelectSuggestion(
        override val isActive: Boolean,
        override val suggestions: List<Suggestion>,
        val selectedSuggestion: Suggestion.ConstantString?
    ) : CodeBlockChild() {
        override fun toReplyString(): String =
            selectedSuggestion?.text ?: ""
    }
}