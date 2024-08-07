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
        val activeCodeBlock = state.activeCodeBlockIndex()?.let { state.codeBlocks[it] }

        val suggestions =
            when (activeCodeBlock) {
                is CodeBlock.Blank -> activeCodeBlock.suggestions
                is CodeBlock.Print ->
                    if (activeCodeBlock.select?.selectedSuggestion == null) {
                        activeCodeBlock.select?.suggestions
                    } else {
                        emptyList()
                    }
                is CodeBlock.Variable ->
                    (activeCodeBlock.activeChild() as? CodeBlockChild.SelectSuggestion)?.let {
                        if (it.selectedSuggestion == null) {
                            it.suggestions
                        } else {
                            emptyList()
                        }
                    }
                null -> emptyList()
            } ?: emptyList()

        val isDeleteButtonEnabled =
            when (activeCodeBlock) {
                is CodeBlock.Blank -> codeBlocks.size > 1
                is CodeBlock.Print -> true
                is CodeBlock.Variable -> {
                    val activeChild = activeCodeBlock.activeChild() as? CodeBlockChild.SelectSuggestion
                    if (activeChild?.selectedSuggestion == null &&
                        activeCodeBlock.children.any { it.selectedSuggestion != null }
                    ) {
                        false
                    } else {
                        true
                    }
                }
                null -> false
            }

        return StepQuizCodeBlanksViewState.Content(
            codeBlocks = codeBlocks,
            suggestions = suggestions,
            isDeleteButtonEnabled = isDeleteButtonEnabled
        )
    }

    private fun mapCodeBlock(
        index: Int,
        codeBlock: CodeBlock
    ): StepQuizCodeBlanksViewState.CodeBlockItem =
        when (codeBlock) {
            is CodeBlock.Blank ->
                StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = index, isActive = codeBlock.isActive)
            is CodeBlock.Print ->
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = index,
                    children = codeBlock.children.mapIndexed(::mapCodeBlockChild)
                )
            is CodeBlock.Variable ->
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = index,
                    children = codeBlock.children.mapIndexed(::mapCodeBlockChild)
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