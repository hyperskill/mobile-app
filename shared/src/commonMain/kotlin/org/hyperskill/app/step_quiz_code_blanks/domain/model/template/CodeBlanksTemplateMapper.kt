package org.hyperskill.app.step_quiz_code_blanks.domain.model.template

import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksResolver
import org.hyperskill.app.step_quiz_code_blanks.presentation.codeBlanksOperationsSuggestions
import org.hyperskill.app.step_quiz_code_blanks.presentation.codeBlanksStringsSuggestions
import org.hyperskill.app.step_quiz_code_blanks.presentation.codeBlanksVariablesSuggestions

internal object CodeBlanksTemplateMapper {
    private const val MATH_EXPRESSIONS_TEMPLATE_STEP_ID = 47580L // ALTAPPS-1324

    fun map(step: Step): List<CodeBlock> =
        when {
            step.id == MATH_EXPRESSIONS_TEMPLATE_STEP_ID -> createMathExpressionsCodeBlocks(step)
            isCodeBlanksTemplateAvailable(step) -> parseCodeBlanksTemplate(step)
            else -> emptyList()
        }

    private fun isCodeBlanksTemplateAvailable(step: Step): Boolean {
        val codeBlockTemplateEntries = step.block.options.codeBlanksTemplate ?: return false
        return codeBlockTemplateEntries.none { it.type == CodeBlockTemplateEntryType.UNKNOWN }
    }

    private fun parseCodeBlanksTemplate(step: Step): List<CodeBlock> {
        val codeBlockTemplateEntries = step.block.options.codeBlanksTemplate
            ?.filter { it.type != CodeBlockTemplateEntryType.UNKNOWN }
        return if (codeBlockTemplateEntries.isNullOrEmpty()) {
            emptyList()
        } else {
            codeBlockTemplateEntries.map { mapCodeBlockTemplateEntry(entry = it, step = step) }
        }
    }

    private fun mapCodeBlockTemplateEntry(
        entry: CodeBlockTemplateEntry,
        step: Step
    ): CodeBlock {
        val childrenSuggestions = step.codeBlanksVariablesSuggestions() +
            step.codeBlanksStringsSuggestions() +
            step.codeBlanksOperationsSuggestions()
        return when (entry.type) {
            CodeBlockTemplateEntryType.BLANK ->
                CodeBlock.Blank(
                    isActive = entry.isActive,
                    indentLevel = entry.indentLevel,
                    suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                        isVariableSuggestionAvailable = StepQuizCodeBlanksResolver.isVariableSuggestionsAvailable(step)
                    )
                )
            CodeBlockTemplateEntryType.PRINT ->
                CodeBlock.Print(
                    indentLevel = entry.indentLevel,
                    children = mapCodeBlockTemplateEntryChildren(
                        entry = entry,
                        suggestions = childrenSuggestions,
                        minimumRequiredChildrenCount = 1
                    )
                )
            CodeBlockTemplateEntryType.VARIABLE ->
                CodeBlock.Variable(
                    indentLevel = entry.indentLevel,
                    children = mapCodeBlockTemplateEntryChildren(
                        entry = entry,
                        suggestions = childrenSuggestions,
                        minimumRequiredChildrenCount = 2
                    )
                )
            CodeBlockTemplateEntryType.IF ->
                CodeBlock.IfStatement(
                    indentLevel = entry.indentLevel,
                    children = mapCodeBlockTemplateEntryChildren(
                        entry = entry,
                        suggestions = childrenSuggestions,
                        minimumRequiredChildrenCount = 1
                    )
                )
            CodeBlockTemplateEntryType.ELIF ->
                CodeBlock.ElifStatement(
                    indentLevel = entry.indentLevel,
                    children = mapCodeBlockTemplateEntryChildren(
                        entry = entry,
                        suggestions = childrenSuggestions,
                        minimumRequiredChildrenCount = 1
                    )
                )
            CodeBlockTemplateEntryType.ELSE ->
                CodeBlock.ElseStatement(
                    isActive = entry.isActive,
                    indentLevel = entry.indentLevel
                )
            CodeBlockTemplateEntryType.UNKNOWN -> error("Unknown code block template entry type")
        }
    }

    private fun mapCodeBlockTemplateEntryChildren(
        entry: CodeBlockTemplateEntry,
        suggestions: List<Suggestion.ConstantString>,
        minimumRequiredChildrenCount: Int,
    ): List<CodeBlockChild.SelectSuggestion> {
        val mappedChildren = entry.children.mapIndexed { index, text ->
            CodeBlockChild.SelectSuggestion(
                isActive = entry.isActive && index == 0,
                suggestions = suggestions,
                selectedSuggestion = Suggestion.ConstantString(text)
            )
        }
        return if (mappedChildren.size < minimumRequiredChildrenCount) {
            val missingChildrenCount = minimumRequiredChildrenCount - mappedChildren.size
            val missingChildren = List(missingChildrenCount) {
                CodeBlockChild.SelectSuggestion(
                    isActive = false,
                    suggestions = suggestions,
                    selectedSuggestion = null
                )
            }
            mappedChildren + missingChildren
        } else {
            mappedChildren
        }
    }

    private fun createMathExpressionsCodeBlocks(step: Step): List<CodeBlock> =
        listOf(
            CodeBlock.Variable(
                indentLevel = 0,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = step.codeBlanksVariablesSuggestions(),
                        selectedSuggestion = Suggestion.ConstantString("x")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = step.codeBlanksStringsSuggestions(),
                        selectedSuggestion = Suggestion.ConstantString("1000")
                    )
                )
            ),
            CodeBlock.Variable(
                indentLevel = 0,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = step.codeBlanksVariablesSuggestions(),
                        selectedSuggestion = Suggestion.ConstantString("r")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = step.codeBlanksStringsSuggestions(),
                        selectedSuggestion = Suggestion.ConstantString("5")
                    )
                )
            ),
            CodeBlock.Variable(
                indentLevel = 0,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = step.codeBlanksVariablesSuggestions(),
                        selectedSuggestion = Suggestion.ConstantString("y")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = step.codeBlanksStringsSuggestions(),
                        selectedSuggestion = Suggestion.ConstantString("10")
                    )
                )
            ),
            CodeBlock.Blank(
                isActive = true,
                indentLevel = 0,
                suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                    isVariableSuggestionAvailable = StepQuizCodeBlanksResolver.isVariableSuggestionsAvailable(step)
                )
            )
        )
}