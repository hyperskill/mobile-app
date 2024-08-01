package org.hyperskill.step_quiz_code_blanks

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.view.mapper.StepQuizCodeBlanksViewStateMapper
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksViewStateMapperTest {
    @Test
    fun `map should return Idle view state for Idle state`() {
        val state = StepQuizCodeBlanksFeature.State.Idle
        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)
        assertEquals(StepQuizCodeBlanksViewState.Idle, viewState)
    }

    @Test
    fun `Content with print suggestion and disabled delete button when active code block is Blank`() {
        val state = stubState(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true))
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = true)),
            suggestions = listOf(Suggestion.Print),
            isDeleteButtonEnabled = false
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with suggestions and enabled delete button when active code block is Print`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = true,
                    selectedSuggestion = null,
                    suggestions = suggestions
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(id = 0, isActive = true, output = null)
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true
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
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = false,
                    selectedSuggestion = printSuggestions[0],
                    suggestions = printSuggestions
                ),
                CodeBlock.Blank(isActive = true)
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    isActive = false,
                    output = printSuggestions[0].text
                ),
                StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 1, isActive = true)
            ),
            suggestions = listOf(Suggestion.Print),
            isDeleteButtonEnabled = false
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
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = false,
                    selectedSuggestion = printSuggestions[0],
                    suggestions = printSuggestions
                ),
                CodeBlock.Print(
                    isActive = true,
                    selectedSuggestion = null,
                    suggestions = printSuggestions
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    isActive = false,
                    output = printSuggestions[0].text
                ),
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(id = 1, isActive = true, output = null)
            ),
            suggestions = printSuggestions,
            isDeleteButtonEnabled = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    private fun stubState(codeBlocks: List<CodeBlock>): StepQuizCodeBlanksFeature.State.Content =
        StepQuizCodeBlanksFeature.State.Content(
            step = Step.stub(id = 0),
            codeBlocks = codeBlocks
        )
}