package org.hyperskill.app.step_quiz_code_blanks.domain.model

import org.hyperskill.app.core.utils.indexOfFirstOrNull

sealed class CodeBlock {
    internal abstract val isActive: Boolean

    internal abstract val suggestions: List<Suggestion>

    internal abstract val children: List<CodeBlockChild>

    internal abstract val analyticRepresentation: String

    internal abstract fun toReplyString(): String

    internal fun activeChild(): CodeBlockChild? =
        children.firstOrNull { it.isActive }

    internal fun activeChildIndex(): Int? =
        children.indexOfFirstOrNull { it.isActive }

    internal data class Blank(
        override val isActive: Boolean,
        override val suggestions: List<Suggestion>
    ) : CodeBlock() {
        override val children: List<CodeBlockChild> = emptyList()

        override val analyticRepresentation: String
            get() = "Blank(isActive=$isActive, suggestions=$suggestions)"

        override fun toReplyString(): String = ""
    }

    internal data class Print(
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String =
            "Print(children=$children)"

        override fun toReplyString(): String =
            buildString {
                append("print(")

                val childrenReplyString = buildString {
                    children.forEachIndexed { index, child ->
                        append(child.toReplyString())

                        val nextChild = children.getOrNull(index + 1)

                        val shouldAppendSpace = when {
                            child.selectedSuggestion?.isOpeningParentheses == true -> false
                            nextChild?.selectedSuggestion?.isClosingParentheses == true -> false
                            else -> index < children.lastIndex
                        }
                        if (shouldAppendSpace) {
                            append(" ")
                        }
                    }
                }
                append(childrenReplyString)

                append(")")
            }

        override fun toString(): String =
            "Print(children=$children)"
    }

    internal data class Variable(
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        val name: CodeBlockChild.SelectSuggestion?
            get() = children.firstOrNull()

        val value: CodeBlockChild.SelectSuggestion?
            get() = children.lastOrNull()

        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String
            get() = "Variable(children=$children)"

        override fun toReplyString(): String =
            buildString {
                append(name?.toReplyString() ?: "")
                append(" = ")
                append(value?.toReplyString() ?: "")
            }

        override fun toString(): String =
            "Variable(children=$children)"
    }
}