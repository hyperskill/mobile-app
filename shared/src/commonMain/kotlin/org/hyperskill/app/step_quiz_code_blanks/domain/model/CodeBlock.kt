package org.hyperskill.app.step_quiz_code_blanks.domain.model

import org.hyperskill.app.core.utils.indexOfFirstOrNull

sealed class CodeBlock {
    companion object;

    internal abstract val isActive: Boolean

    internal abstract val indentLevel: Int

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
        override val indentLevel: Int,
        override val suggestions: List<Suggestion>
    ) : CodeBlock() {
        override val children: List<CodeBlockChild> = emptyList()

        override val analyticRepresentation: String
            get() = "Blank(isActive=$isActive, suggestions=$suggestions)"

        override fun toReplyString(): String = ""
    }

    internal data class Print(
        override val indentLevel: Int,
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String =
            "Print(children=$children)"

        override fun toReplyString(): String =
            buildString {
                append("print(")
                append(joinChildrenToReplyString(children))
                append(")")
            }

        override fun toString(): String =
            "Print(children=$children)"
    }

    internal data class Variable(
        override val indentLevel: Int,
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        val name: CodeBlockChild.SelectSuggestion?
            get() = children.firstOrNull()

        val values: List<CodeBlockChild.SelectSuggestion>
            get() = children.drop(1)

        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String
            get() = "Variable(children=$children)"

        override fun toReplyString(): String =
            buildString {
                append(name?.toReplyString() ?: "")
                append(" = ")
                append(joinChildrenToReplyString(values))
            }

        override fun toString(): String =
            "Variable(children=$children)"
    }

    internal data class IfStatement(
        override val indentLevel: Int,
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String
            get() = "IfStatement(children=$children)"

        override fun toReplyString(): String =
            buildString {
                append("if ")
                append(joinChildrenToReplyString(children))
                append(":")
            }

        override fun toString(): String =
            "IfStatement(children=$children)"
    }
}

internal fun CodeBlock.Companion.joinChildrenToReplyString(children: List<CodeBlockChild>): String =
    buildString {
        children.forEachIndexed { index, child ->
            append(child.toReplyString())

            val nextChild = children.getOrNull(index + 1)

            val shouldAppendSpace = when {
                (child as? CodeBlockChild.SelectSuggestion)?.selectedSuggestion?.isOpeningParentheses == true ->
                    false
                (nextChild as? CodeBlockChild.SelectSuggestion)?.selectedSuggestion?.isClosingParentheses == true ->
                    false
                else -> index < children.lastIndex
            }
            if (shouldAppendSpace) {
                append(" ")
            }
        }
    }