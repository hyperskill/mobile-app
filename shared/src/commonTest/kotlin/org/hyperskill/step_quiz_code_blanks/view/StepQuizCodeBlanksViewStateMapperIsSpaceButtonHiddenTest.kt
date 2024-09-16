package org.hyperskill.step_quiz_code_blanks.view

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.view.mapper.StepQuizCodeBlanksViewStateMapper
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step.domain.model.stub
import org.hyperskill.step_quiz_code_blanks.presentation.stub

class StepQuizCodeBlanksViewStateMapperIsSpaceButtonHiddenTest {
    private val step = Step.stub(
        id = 0,
        block = Block.stub(
            options = Block.Options(
                codeBlanksOperations = listOf("+")
            )
        )
    )

    @Test
    fun `isSpaceButtonHidden should be true when codeBlanksOperationsSuggestions is empty`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = Step.stub(id = 0),
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when no active code block`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = false,
                    suggestions = emptyList()
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when active Print code block has no active child`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = emptyList(),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when active Print code block child has no selected suggestion`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = listOf(
                CodeBlock.Print(
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
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be false when active Print code block child has selected suggestion`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isSpaceButtonHidden)
    }

    /* ktlint-disable */
    @Test
    fun `isSpaceButtonHidden should be true when active Variable code block's first child has no selected suggestion`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = emptyList(),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when active Variable code block's second child has no selected suggestion`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("x")
                        ),
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
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be false when active IfStatement code block child has selected suggestion`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("if")
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when active IfStatement code block child has no selected suggestion`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = listOf(
                CodeBlock.IfStatement(
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
        assertTrue(viewState.isSpaceButtonHidden)
    }
}