package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer

class StepQuizCodeBlanksReducerEnterButtonClickedTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `EnterButtonClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `EnterButtonClicked should not update state if no active code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = false,
                    suggestions = emptyList()
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        assertEquals(initialState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `EnterButtonClicked should append new active Blank block if active code block exists`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = emptyList()
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
                CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print))
            )
        )

        assertEquals(expectedState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `EnterButtonClicked should add new active Blank block after active code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = true, suggestions = emptyList()),
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

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
                CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)),
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

        assertEquals(expectedState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `EnterButtonClicked should add Blank with next indentLevel if active code block is condition`() {
        val codeBlocks = listOf(
            CodeBlock.IfStatement(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = null
                    )
                )
            ),
            CodeBlock.ElifStatement(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = null
                    )
                )
            ),
            CodeBlock.ElseStatement(isActive = true)
        )

        codeBlocks.forEach { codeBlock ->
            val initialState = StepQuizCodeBlanksFeature.State.Content.stub(codeBlocks = listOf(codeBlock))

            val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

            assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
            assertEquals(1, state.codeBlocks[1].indentLevel)
            assertContainsEnterButtonClickedAnalyticEvent(actions)
        }
    }

    private fun assertContainsEnterButtonClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent
            }
        }
    }
}