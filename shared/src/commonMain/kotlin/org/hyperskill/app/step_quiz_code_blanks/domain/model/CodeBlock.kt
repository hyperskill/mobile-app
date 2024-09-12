package org.hyperskill.app.step_quiz_code_blanks.domain.model

import org.hyperskill.app.core.utils.indexOfFirstOrNull
import ru.nobird.app.core.model.cast

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

    internal fun areAllChildrenUnselected(): Boolean =
        children.all { it is CodeBlockChild.SelectSuggestion && it.selectedSuggestion == null }

    internal fun areAllChildrenSelected(): Boolean =
        children.all { it is CodeBlockChild.SelectSuggestion && it.selectedSuggestion != null }

    internal fun hasAnySelectedChild(): Boolean =
        children.any { it is CodeBlockChild.SelectSuggestion && it.selectedSuggestion != null }

    internal fun hasAnyUnselectedChild(): Boolean =
        children.any { it is CodeBlockChild.SelectSuggestion && it.selectedSuggestion == null }

    internal data class Blank(
        override val isActive: Boolean,
        override val indentLevel: Int = 0,
        override val suggestions: List<Suggestion>
    ) : CodeBlock() {
        override val children: List<CodeBlockChild> = emptyList()

        override val analyticRepresentation: String
            get() = "Blank(isActive=$isActive, indentLevel=$indentLevel, suggestions=$suggestions)"

        override fun toReplyString(): String = ""
    }

    internal data class Print(
        override val indentLevel: Int = 0,
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String =
            "Print(indentLevel=$indentLevel, children=$children)"

        override fun toReplyString(): String =
            buildString {
                append(buildIndentString(indentLevel))
                append("print(")
                append(joinChildrenToReplyString(children))
                append(")")
            }

        override fun toString(): String =
            "Print(indentLevel=$indentLevel, children=$children)"
    }

    internal data class Variable(
        override val indentLevel: Int = 0,
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        val name: CodeBlockChild.SelectSuggestion?
            get() = children.firstOrNull()

        val values: List<CodeBlockChild.SelectSuggestion>
            get() = children.drop(1)

        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String
            get() = "Variable(indentLevel=$indentLevel, children=$children)"

        override fun toReplyString(): String =
            buildString {
                append(buildIndentString(indentLevel))
                append(name?.toReplyString() ?: "")
                append(" = ")
                append(joinChildrenToReplyString(values))
            }

        override fun toString(): String =
            "Variable(indentLevel=$indentLevel, children=$children)"
    }

    internal data class IfStatement(
        override val indentLevel: Int = 0,
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String
            get() = "IfStatement(indentLevel=$indentLevel, children=$children)"

        override fun toReplyString(): String =
            buildString {
                append(buildIndentString(indentLevel))
                append("if ")
                append(joinChildrenToReplyString(children))
                append(":")
            }

        override fun toString(): String =
            "IfStatement(indentLevel=$indentLevel, children=$children)"
    }

    internal data class ElifStatement(
        override val indentLevel: Int = 0,
        override val children: List<CodeBlockChild.SelectSuggestion>
    ) : CodeBlock() {
        override val isActive: Boolean = false

        override val suggestions: List<Suggestion> = emptyList()

        override val analyticRepresentation: String
            get() = "ElifStatement(indentLevel=$indentLevel, children=$children)"

        override fun toReplyString(): String =
            buildString {
                append("elif ")
                append(joinChildrenToReplyString(children))
                append(":")
            }

        override fun toString(): String =
            "ElifStatement(indentLevel=$indentLevel, children=$children)"
    }

    internal data class ElseStatement(
        override val isActive: Boolean,
        override val indentLevel: Int = 0
    ) : CodeBlock() {
        override val suggestions: List<Suggestion> = emptyList()

        override val children: List<CodeBlockChild> = emptyList()

        override val analyticRepresentation: String
            get() = "ElseStatement(isActive=$isActive, indentLevel=$indentLevel)"

        override fun toReplyString(): String = "else:"

        override fun toString(): String =
            "ElseStatement(isActive=$isActive, indentLevel=$indentLevel)"
    }
}

internal fun CodeBlock.Companion.buildIndentString(indentLevel: Int): String =
    "\t".repeat(indentLevel)

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

internal fun CodeBlock.updatedChildren(children: List<CodeBlockChild>): CodeBlock =
    when (this) {
        is CodeBlock.Blank,
        is CodeBlock.ElseStatement -> this
        is CodeBlock.Print -> copy(children = children.cast())
        is CodeBlock.Variable -> copy(children = children.cast())
        is CodeBlock.IfStatement -> copy(children = children.cast())
        is CodeBlock.ElifStatement -> copy(children = children.cast())
    }

internal fun CodeBlock.updatedIndentLevel(indentLevel: Int): CodeBlock =
    when (this) {
        is CodeBlock.Blank -> copy(indentLevel = indentLevel)
        is CodeBlock.Print -> copy(indentLevel = indentLevel)
        is CodeBlock.Variable -> copy(indentLevel = indentLevel)
        is CodeBlock.IfStatement -> copy(indentLevel = indentLevel)
        is CodeBlock.ElifStatement -> copy(indentLevel = indentLevel)
        is CodeBlock.ElseStatement -> copy(indentLevel = indentLevel)
    }