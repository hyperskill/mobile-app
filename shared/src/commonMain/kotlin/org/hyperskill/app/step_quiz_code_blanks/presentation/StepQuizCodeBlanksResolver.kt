package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import ru.nobird.app.core.model.slice

internal object StepQuizCodeBlanksResolver {
    private const val MINIMUM_POSSIBLE_INDEX_FOR_ELIF_AND_ELSE_STATEMENTS = 2

    fun isVariableSuggestionsAvailable(step: Step): Boolean =
        step.block.options.codeBlanksVariables?.isNotEmpty() == true

    fun getSuggestionsForBlankCodeBlock(
        index: Int = -1,
        indentLevel: Int = 0,
        codeBlocks: List<CodeBlock> = emptyList(),
        isVariableSuggestionAvailable: Boolean
    ): List<Suggestion> =
        when {
            areElifAndElseStatementsSuggestionsAvailable(index, indentLevel, codeBlocks) ->
                listOf(Suggestion.Print, Suggestion.Variable, Suggestion.ElifStatement, Suggestion.ElseStatement)

            isVariableSuggestionAvailable ->
                listOf(Suggestion.Print, Suggestion.Variable, Suggestion.IfStatement)

            else ->
                listOf(Suggestion.Print)
        }

    fun areElifAndElseStatementsSuggestionsAvailable(
        index: Int,
        indentLevel: Int,
        codeBlocks: List<CodeBlock>
    ): Boolean {
        if (index < MINIMUM_POSSIBLE_INDEX_FOR_ELIF_AND_ELSE_STATEMENTS || codeBlocks.isEmpty()) {
            return false
        }

        val previousCodeBlock = codeBlocks
            .slice(to = index)
            .reversed()
            .firstOrNull { it.indentLevel == indentLevel }

        return when (previousCodeBlock) {
            is CodeBlock.IfStatement,
            is CodeBlock.ElifStatement -> true
            else -> false
        }
    }
}