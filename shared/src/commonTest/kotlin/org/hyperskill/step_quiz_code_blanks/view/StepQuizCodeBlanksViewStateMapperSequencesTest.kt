package org.hyperskill.step_quiz_code_blanks.view

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.view.mapper.StepQuizCodeBlanksViewStateMapper
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step_quiz_code_blanks.presentation.stub

class StepQuizCodeBlanksViewStateMapperSequencesTest {
    @Test
    fun `map should return Idle view state for Idle state`() {
        val state = StepQuizCodeBlanksFeature.State.Idle
        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)
        assertEquals(StepQuizCodeBlanksViewState.Idle, viewState)
    }

    @Test
    fun `Content with active not filled Print`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with sequence of filled Print and active Blank`() {
        val printSuggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = printSuggestions,
                            selectedSuggestion = printSuggestions[0]
                        )
                    )
                ),
                CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print))
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = printSuggestions[0].text
                        )
                    )
                ),
                StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 1, isActive = true)
            ),
            suggestions = listOf(Suggestion.Print),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with sequence of filled Print and active not filled Print`() {
        val printSuggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = printSuggestions,
                            selectedSuggestion = printSuggestions[0]
                        )
                    )
                ),
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = printSuggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = printSuggestions[0].text
                        )
                    )
                ),
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 1,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = printSuggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }
}