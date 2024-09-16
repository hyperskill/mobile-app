package org.hyperskill.app.step_quiz_code_blanks.view.mapper

import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.activeCodeBlockIndex
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState

object StepQuizCodeBlanksViewStateMapper {
    fun map(state: StepQuizCodeBlanksFeature.State): StepQuizCodeBlanksViewState =
        when (state) {
            StepQuizCodeBlanksFeature.State.Idle -> StepQuizCodeBlanksViewState.Idle
            is StepQuizCodeBlanksFeature.State.Content -> mapContentState(state)
        }

    private fun mapContentState(
        state: StepQuizCodeBlanksFeature.State.Content
    ): StepQuizCodeBlanksViewState.Content {
        val codeBlocks = state.codeBlocks.mapIndexed(::mapCodeBlock)

        val activeCodeBlockIndex = state.activeCodeBlockIndex()
        val activeCodeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] }

        val suggestions =
            when (activeCodeBlock) {
                is CodeBlock.Blank ->
                    activeCodeBlock.suggestions

                is CodeBlock.Print,
                is CodeBlock.Variable,
                is CodeBlock.IfStatement,
                is CodeBlock.ElifStatement ->
                    (activeCodeBlock.activeChild() as? CodeBlockChild.SelectSuggestion)?.let {
                        if (it.selectedSuggestion == null) {
                            it.suggestions
                        } else {
                            emptyList()
                        }
                    }

                null,
                is CodeBlock.ElseStatement -> emptyList()
            } ?: emptyList()

        val isDeleteButtonEnabled = activeCodeBlock?.isDeleteForbidden == false &&
            when (activeCodeBlock) {
                is CodeBlock.Blank -> codeBlocks.size > 1
                is CodeBlock.Print -> true
                is CodeBlock.Variable -> {
                    activeCodeBlock.activeChildIndex()?.let { activeChildIndex ->
                        when {
                            activeChildIndex == 0 || activeChildIndex > 1 ->
                                true

                            activeCodeBlock.children[activeChildIndex].selectedSuggestion == null &&
                                activeCodeBlock.hasAnySelectedChild() ->
                                false

                            else -> true
                        }
                    } ?: false
                }
                is CodeBlock.IfStatement,
                is CodeBlock.ElifStatement -> {
                    activeCodeBlock.activeChildIndex()?.let { activeChildIndex ->
                        val activeChild = activeCodeBlock.children[activeChildIndex] as CodeBlockChild.SelectSuggestion

                        when {
                            activeChildIndex > 0 ->
                                true

                            activeChild.selectedSuggestion != null ->
                                true

                            else ->
                                codeBlocks.getOrNull(activeCodeBlockIndex + 1)
                                    ?.let { it.indentLevel == activeCodeBlock.indentLevel } ?: true
                        }
                    } ?: false
                }
                is CodeBlock.ElseStatement ->
                    codeBlocks.getOrNull(activeCodeBlockIndex + 1)
                        ?.let { it.indentLevel == activeCodeBlock.indentLevel } ?: true
                null -> false
            }

        val isSpaceButtonHidden = if (state.codeBlanksOperationsSuggestions.isNotEmpty()) {
            when (activeCodeBlock) {
                is CodeBlock.Print,
                is CodeBlock.IfStatement,
                is CodeBlock.ElifStatement -> {
                    val activeChild = activeCodeBlock.activeChild() as? CodeBlockChild.SelectSuggestion
                    activeChild?.selectedSuggestion == null
                }
                is CodeBlock.Variable -> {
                    val activeChildIndex = activeCodeBlock.activeChildIndex()
                    if (activeChildIndex != null && activeChildIndex > 0) {
                        activeCodeBlock.children[activeChildIndex].selectedSuggestion == null
                    } else {
                        true
                    }
                }
                null,
                is CodeBlock.Blank,
                is CodeBlock.ElseStatement -> true
            }
        } else {
            true
        }

        val isPreviousCodeBlockCondition =
            when (activeCodeBlockIndex?.let { state.codeBlocks.getOrNull(it - 1) }) {
                is CodeBlock.IfStatement,
                is CodeBlock.ElifStatement,
                is CodeBlock.ElseStatement -> true
                else -> false
            }
        val isDecreaseIndentLevelButtonHidden =
            when {
                activeCodeBlock == null -> true
                activeCodeBlock.indentLevel < 1 -> true
                isPreviousCodeBlockCondition -> true
                else -> false
            }

        return StepQuizCodeBlanksViewState.Content(
            codeBlocks = codeBlocks,
            suggestions = suggestions,
            isDeleteButtonEnabled = isDeleteButtonEnabled,
            isSpaceButtonHidden = isSpaceButtonHidden,
            isDecreaseIndentLevelButtonHidden = isDecreaseIndentLevelButtonHidden,
            onboardingState = state.onboardingState
        )
    }

    private fun mapCodeBlock(
        index: Int,
        codeBlock: CodeBlock
    ): StepQuizCodeBlanksViewState.CodeBlockItem =
        when (codeBlock) {
            is CodeBlock.Blank ->
                StepQuizCodeBlanksViewState.CodeBlockItem.Blank(
                    id = index,
                    indentLevel = codeBlock.indentLevel,
                    isActive = codeBlock.isActive
                )
            is CodeBlock.Print ->
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = index,
                    indentLevel = codeBlock.indentLevel,
                    children = codeBlock.children.mapIndexed(::mapCodeBlockChild)
                )
            is CodeBlock.Variable ->
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = index,
                    indentLevel = codeBlock.indentLevel,
                    children = codeBlock.children.mapIndexed(::mapCodeBlockChild)
                )
            is CodeBlock.IfStatement ->
                StepQuizCodeBlanksViewState.CodeBlockItem.IfStatement(
                    id = index,
                    indentLevel = codeBlock.indentLevel,
                    children = codeBlock.children.mapIndexed(::mapCodeBlockChild)
                )
            is CodeBlock.ElifStatement ->
                StepQuizCodeBlanksViewState.CodeBlockItem.ElifStatement(
                    id = index,
                    indentLevel = codeBlock.indentLevel,
                    children = codeBlock.children.mapIndexed(::mapCodeBlockChild)
                )
            is CodeBlock.ElseStatement ->
                StepQuizCodeBlanksViewState.CodeBlockItem.ElseStatement(
                    id = index,
                    indentLevel = codeBlock.indentLevel,
                    isActive = codeBlock.isActive
                )
        }

    private fun mapCodeBlockChild(
        index: Int,
        codeBlockChild: CodeBlockChild
    ): StepQuizCodeBlanksViewState.CodeBlockChildItem =
        when (codeBlockChild) {
            is CodeBlockChild.SelectSuggestion ->
                StepQuizCodeBlanksViewState.CodeBlockChildItem(
                    id = index,
                    isActive = codeBlockChild.isActive,
                    value = codeBlockChild.selectedSuggestion?.text
                )
        }
}