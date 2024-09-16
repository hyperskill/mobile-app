package org.hyperskill.step_quiz_code_blanks.view

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.view.mapper.StepQuizCodeBlanksViewStateMapper
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step_quiz_code_blanks.presentation.stub

class StepQuizCodeBlanksViewStateMapperSuggestionsTest {
    @Test
    fun `Non empty suggestions when active code block is Blank`() {
        val suggestions = listOf(Suggestion.Print, Suggestion.Variable)
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = suggestions))
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertEquals(suggestions, viewState.suggestions)
    }

    @Test
    fun `Empty suggestions when code block active child has selected suggestion`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val children = listOf(
            CodeBlockChild.SelectSuggestion(
                isActive = true,
                suggestions = suggestions,
                selectedSuggestion = suggestions[0]
            )
        )
        val codeBlocks = listOf(
            CodeBlock.Print(children = children),
            CodeBlock.Variable(children = children),
            CodeBlock.IfStatement(children = children)
        )

        codeBlocks.forEach { codeBlock ->
            val state = StepQuizCodeBlanksFeature.State.Content.stub(codeBlocks = listOf(codeBlock))
            val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

            assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
            assertTrue(viewState.suggestions.isEmpty())
        }
    }

    @Test
    fun `Non empty suggestions when code block active child is unselected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val children = listOf(
            CodeBlockChild.SelectSuggestion(
                isActive = true,
                suggestions = suggestions,
                selectedSuggestion = null
            )
        )
        val codeBlocks = listOf(
            CodeBlock.Print(children = children),
            CodeBlock.Variable(children = children),
            CodeBlock.IfStatement(children = children)
        )

        codeBlocks.forEach { codeBlock ->
            val state = StepQuizCodeBlanksFeature.State.Content.stub(codeBlocks = listOf(codeBlock))
            val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

            assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
            assertEquals(suggestions, viewState.suggestions)
        }
    }

    @Test
    fun `Non empty suggestions when active code block is Variable and active child has no selected suggestion`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertEquals(suggestions, viewState.suggestions)
    }
}