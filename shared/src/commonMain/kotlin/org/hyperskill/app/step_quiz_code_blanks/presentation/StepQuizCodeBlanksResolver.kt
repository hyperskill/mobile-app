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
        isVariableSuggestionAvailable: Boolean,
        availableConditions: Set<String>
    ): List<Suggestion> =
        when {
            areElifAndElseStatementsSuggestionsAvailable(index, indentLevel, codeBlocks, availableConditions) ->
                buildList {
                    add(Suggestion.Print)
                    add(Suggestion.Variable)

                    if (availableConditions.contains(Suggestion.ElifStatement.text)) {
                        add(Suggestion.ElifStatement)
                    }

                    if (availableConditions.contains(Suggestion.ElseStatement.text)) {
                        add(Suggestion.ElseStatement)
                    }
                }

            isVariableSuggestionAvailable ->
                buildList {
                    add(Suggestion.Print)
                    add(Suggestion.Variable)

                    if (availableConditions.contains(Suggestion.IfStatement.text)) {
                        add(Suggestion.IfStatement)
                    }
                }

            else ->
                listOf(Suggestion.Print)
        }

    fun areElifAndElseStatementsSuggestionsAvailable(
        index: Int,
        indentLevel: Int,
        codeBlocks: List<CodeBlock>,
        availableConditions: Set<String>
    ): Boolean {
        if (
            index < MINIMUM_POSSIBLE_INDEX_FOR_ELIF_AND_ELSE_STATEMENTS ||
            codeBlocks.isEmpty() ||
            availableConditions.isEmpty()
        ) {
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