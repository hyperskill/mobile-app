package org.hyperskill.app.step_quiz_code_blanks.view.mapper

import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
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
        val activeCodeBlock = state.codeBlocks.firstOrNull { it.isActive }

        val suggestions =
            when (activeCodeBlock) {
                is CodeBlock.Blank -> activeCodeBlock.suggestions
                is CodeBlock.Print ->
                    if (activeCodeBlock.selectedSuggestion == null) {
                        activeCodeBlock.suggestions
                    } else {
                        emptyList()
                    }
                null -> emptyList()
            }

        val isDeleteButtonEnabled =
            when (activeCodeBlock) {
                is CodeBlock.Blank -> codeBlocks.size > 1
                is CodeBlock.Print -> true
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
                    isActive = codeBlock.isActive,
                    output = codeBlock.selectedSuggestion?.text
                )
        }
}