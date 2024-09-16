package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState

class StepQuizCodeBlanksReducerCodeBlockClickedTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `CodeBlockClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = true)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `CodeBlockClicked should not update state if no target code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList()))
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 1, isActive = true)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsCodeBlockClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockClicked should not update state if target code block is active`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList()))
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = true)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsCodeBlockClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockClicked should update active Print code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = false)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = true, suggestions = emptyList()),
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsCodeBlockClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockClicked should update active ElseStatement code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
                CodeBlock.ElseStatement(isActive = true)
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = false)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = true, suggestions = emptyList()),
                CodeBlock.ElseStatement(isActive = false)
            )
        )

        assertEquals(expectedState, state)
        assertContainsCodeBlockClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockClicked should update active Variable code block to not active`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true, suggestions = emptyList(), selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = false)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsCodeBlockClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockClicked should update not active Variable code block to active`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 1, isActive = false)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true, suggestions = emptyList(), selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsCodeBlockClickedAnalyticEvent(actions)
    }

    private fun assertContainsCodeBlockClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
            }
        }
    }
}