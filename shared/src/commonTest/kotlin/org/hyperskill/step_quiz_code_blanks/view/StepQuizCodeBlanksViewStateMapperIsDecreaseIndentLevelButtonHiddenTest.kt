package org.hyperskill.step_quiz_code_blanks.view

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.view.mapper.StepQuizCodeBlanksViewStateMapper
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step_quiz_code_blanks.presentation.stub

class StepQuizCodeBlanksViewStateMapperIsDecreaseIndentLevelButtonHiddenTest {
    @Test
    fun `isDecreaseIndentLevelButtonHidden should be true when no active code block`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = false,
                    suggestions = emptyList()
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be true when active code block's indent level is less than 1`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, indentLevel = 0, suggestions = emptyList()))
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be false when active code block's indent level is 1 or more`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    indentLevel = 1,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be true when previous code block is IfStatement`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.IfStatement(indentLevel = 1, children = emptyList()),
                CodeBlock.Blank(isActive = true, indentLevel = 1, suggestions = emptyList())
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be false when previous code block is not IfStatement`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(indentLevel = 1, children = emptyList()),
                CodeBlock.Print(
                    indentLevel = 1,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isDecreaseIndentLevelButtonHidden)
    }
}