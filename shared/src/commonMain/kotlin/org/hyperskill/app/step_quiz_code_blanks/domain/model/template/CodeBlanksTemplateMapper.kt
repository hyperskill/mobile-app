package org.hyperskill.app.step_quiz_code_blanks.domain.model.template

import kotlin.math.max
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
    ): CodeBlock =
        when (entry.type) {
            CodeBlockTemplateEntryType.BLANK ->
                CodeBlock.Blank(
                    isActive = entry.isActive,
                    indentLevel = entry.indentLevel,
                    isDeleteForbidden = entry.isDeleteForbidden,
                    suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                        isVariableSuggestionAvailable = StepQuizCodeBlanksResolver.isVariableSuggestionsAvailable(step)
                    )
                )
            CodeBlockTemplateEntryType.PRINT ->
                CodeBlock.Print(
                    indentLevel = entry.indentLevel,
                    isDeleteForbidden = entry.isDeleteForbidden,
                    children = mapCodeBlockTemplateEntryChildren(
                        entry = entry,
                        suggestions = getChildrenSuggestions(step),
                        minimumRequiredChildrenCount = 1
                    )
                )
            CodeBlockTemplateEntryType.VARIABLE ->
                CodeBlock.Variable(
                    indentLevel = entry.indentLevel,
                    isDeleteForbidden = entry.isDeleteForbidden,
                    children = mapCodeBlockTemplateEntryChildren(
                        entry = entry,
                        suggestions = getChildrenSuggestions(step),
                        minimumRequiredChildrenCount = 2
                    )
                )
            CodeBlockTemplateEntryType.IF ->
                CodeBlock.IfStatement(
                    indentLevel = entry.indentLevel,
                    isDeleteForbidden = entry.isDeleteForbidden,
                    children = mapCodeBlockTemplateEntryChildren(
                        entry = entry,
                        suggestions = getChildrenSuggestions(step),
                        minimumRequiredChildrenCount = 1
                    )
                )
            CodeBlockTemplateEntryType.ELIF ->
                CodeBlock.ElifStatement(
                    indentLevel = entry.indentLevel,
                    isDeleteForbidden = entry.isDeleteForbidden,
                    children = mapCodeBlockTemplateEntryChildren(
                        entry = entry,
                        suggestions = getChildrenSuggestions(step),
                        minimumRequiredChildrenCount = 1
                    )
                )
            CodeBlockTemplateEntryType.ELSE ->
                CodeBlock.ElseStatement(
                    isActive = entry.isActive,
                    indentLevel = entry.indentLevel,
                    isDeleteForbidden = entry.isDeleteForbidden,
                )
            CodeBlockTemplateEntryType.UNKNOWN -> error("Unknown code block template entry type")
        }

    private fun getChildrenSuggestions(step: Step): List<Suggestion.ConstantString> =
        step.codeBlanksVariablesSuggestions() + step.codeBlanksStringsSuggestions() +
            step.codeBlanksOperationsSuggestions()

    private fun mapCodeBlockTemplateEntryChildren(
        entry: CodeBlockTemplateEntry,
        suggestions: List<Suggestion.ConstantString>,
        minimumRequiredChildrenCount: Int
    ): List<CodeBlockChild.SelectSuggestion> {
        val mappedChildren = entry.children.map { text ->
            CodeBlockChild.SelectSuggestion(
                isActive = false,
                suggestions = suggestions,
                selectedSuggestion = Suggestion.ConstantString(text)
            )
        }

        val missingChildrenCount = max(0, minimumRequiredChildrenCount - mappedChildren.size)
        val missingChildren = List(missingChildrenCount) {
            CodeBlockChild.SelectSuggestion(
                isActive = false,
                suggestions = suggestions,
                selectedSuggestion = null
            )
        }

        val completeChildren = mappedChildren + missingChildren

        return if (entry.isActive) {
            completeChildren.mapIndexed { index, child ->
                child.copy(isActive = entry.isActive && index == 0)
            }
        } else {
            completeChildren
        }
    }

    private fun createMathExpressionsCodeBlocks(step: Step): List<CodeBlock> =
        listOf(
            CodeBlock.Variable(
                indentLevel = 0,
                isDeleteForbidden = true,
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
                isDeleteForbidden = true,
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
                isDeleteForbidden = true,
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
                isDeleteForbidden = false,
                suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                    isVariableSuggestionAvailable = StepQuizCodeBlanksResolver.isVariableSuggestionsAvailable(step)
                )
            )
        )
}