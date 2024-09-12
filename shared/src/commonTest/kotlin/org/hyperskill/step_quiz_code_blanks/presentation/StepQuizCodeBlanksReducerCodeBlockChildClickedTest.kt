package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockChildHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState

class StepQuizCodeBlanksReducerCodeBlockChildClickedTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `CodeBlockChildClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val message = StepQuizCodeBlanksFeature.Message.CodeBlockChildClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Variable(id = 0, children = emptyList()),
            codeBlockChildItem = StepQuizCodeBlanksViewState.CodeBlockChildItem(id = 0, isActive = false, value = null)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `CodeBlockChildClicked should not update state if target code block is not found`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
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

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockChildClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Variable(id = 1, children = emptyList()),
            codeBlockChildItem = StepQuizCodeBlanksViewState.CodeBlockChildItem(id = 0, isActive = false, value = null)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsCodeBlockChildClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockChildClicked should update state to activate the clicked Variable child`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = emptyList(),
                            selectedSuggestion = null
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

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockChildClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Variable(id = 0, children = emptyList()),
            codeBlockChildItem = StepQuizCodeBlanksViewState.CodeBlockChildItem(id = 0, isActive = false, value = null)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
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

        assertEquals(expectedState, state)
        assertContainsCodeBlockChildClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockChildClicked should update state to activate the clicked Print child`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = emptyList(),
                            selectedSuggestion = null
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

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockChildClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Print(id = 0, children = emptyList()),
            codeBlockChildItem = StepQuizCodeBlanksViewState.CodeBlockChildItem(id = 0, isActive = false, value = null)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
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

        assertEquals(expectedState, state)
        assertContainsCodeBlockChildClickedAnalyticEvent(actions)
    }

    private fun assertContainsCodeBlockChildClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedCodeBlockChildHyperskillAnalyticEvent
            }
        }
    }
}