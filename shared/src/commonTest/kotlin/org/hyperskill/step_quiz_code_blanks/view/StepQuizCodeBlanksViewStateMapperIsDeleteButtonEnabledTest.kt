package org.hyperskill.step_quiz_code_blanks.view

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.view.mapper.StepQuizCodeBlanksViewStateMapper
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step_quiz_code_blanks.presentation.stub

class StepQuizCodeBlanksViewStateMapperIsDeleteButtonEnabledTest {
    @Test
    fun `isDeleteButtonEnabled should be false when active code block is Blank and single`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)))
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when active code block is Print and single`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(Suggestion.Print),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when Variable active name is unselected`() {
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
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when Variable active name is selected`() {
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
                            selectedSuggestion = suggestions[0]
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
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when Variable active value child index is greater than one`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[1]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be false when Variable name is selected and active value is unselected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when Variable name is selected and active value is selected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[1]
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when Variable name is unselected and active value is unselected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when Variable name is unselected and active value is selected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when IfStatement active child index greater than zero`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    @Test
    fun `isDeleteButtonEnabled should be true when IfStatement child is selected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    /* ktlint-disable */
    @Test
    fun `isDeleteButtonEnabled should be true when IfStatement child is unselected and next code block on same indent level`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    indentLevel = 1,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Print(
                    indentLevel = 1,
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

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDeleteButtonEnabled)
    }

    /* ktlint-disable */
    @Test
    fun `isDeleteButtonEnabled should be false when IfStatement child is unselected and next code block on different indent level`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    indentLevel = 1,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Print(
                    indentLevel = 2,
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

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isDeleteButtonEnabled)
    }
}