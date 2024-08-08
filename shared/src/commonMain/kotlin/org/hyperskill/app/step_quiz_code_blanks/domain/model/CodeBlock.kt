package org.hyperskill.app.step_quiz_code_blanks.domain.model

sealed class CodeBlock {
    abstract val isActive: Boolean

    abstract val suggestions: List<Suggestion>

    abstract val children: List<CodeBlockChild>

    internal abstract val analyticRepresentation: String

    internal fun activeChild(): CodeBlockChild? =
        children.firstOrNull { it.isActive }

    internal fun activeChildIndex(): Int? =
        children.indexOfFirst { it.isActive }.takeIf { it != -1 }

    data class Blank(
        override val isActive: Boolean,
        override val suggestions: List<Suggestion>
    ) : CodeBlock() {
        override val children: List<CodeBlockChild> = emptyList()

        override val analyticRepresentation: String
            get() = "Blank(isActive=$isActive, suggestions=$suggestions)"

        override fun toString(): String = ""
    }

    data class Print(
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        internal val select: CodeBlockChild.SelectSuggestion?
            get() = children.firstOrNull()

        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String =
            "Print(children=$children, isActive=$isActive, suggestions=$suggestions)"

        override fun toString(): String =
            buildString {
                append("print(")
                append(
                    children.joinToString(separator = ", ") { it.toString() }
                )
                append(")")
            }
    }

    data class Variable(
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        internal val name: CodeBlockChild.SelectSuggestion?
            get() = children.firstOrNull()

        internal val value: CodeBlockChild.SelectSuggestion?
            get() = children.lastOrNull()

        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String
            get() = "Variable(children=$children, isActive=$isActive, suggestions=$suggestions)"

        override fun toString(): String =
            buildString {
                append(name?.toString() ?: "")
                append(" = ")
                append(value?.toString() ?: "")
            }
    }
}