package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDecreaseIndentLevelHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer

class StepQuizCodeBlanksReducerDecreaseIndentLevelButtonClickedTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `DecreaseIndentLevelButtonClicked should not update state if no active code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.Blank(isActive = false, suggestions = emptyList()))
        )

        val (state, actions) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.Message.DecreaseIndentLevelButtonClicked
        )

        assertEquals(initialState, state)
        assertContainsDecreaseIndentLevelAnalyticEvent(actions)
    }

    @Test
    fun `DecreaseIndentLevelButtonClicked should not decrease indent level below 1`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, indentLevel = 0, suggestions = emptyList()))
        )

        val (state, actions) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.Message.DecreaseIndentLevelButtonClicked
        )

        assertEquals(initialState, state)
        assertContainsDecreaseIndentLevelAnalyticEvent(actions)
    }

    @Test
    fun `DecreaseIndentLevelButtonClicked should decrease indent level by 1`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, indentLevel = 1, suggestions = emptyList()))
        )

        val (state, actions) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.Message.DecreaseIndentLevelButtonClicked
        )

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    indentLevel = 0,
                    suggestions = listOf(Suggestion.Print)
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsDecreaseIndentLevelAnalyticEvent(actions)
    }

    @Test
    fun `DecreaseIndentLevelButtonClicked should decrease indent level for active code block only`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, indentLevel = 3, suggestions = emptyList()),
                CodeBlock.Blank(isActive = true, indentLevel = 2, suggestions = emptyList())
            )
        )

        val (state, actions) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.Message.DecreaseIndentLevelButtonClicked
        )

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, indentLevel = 3, suggestions = emptyList()),
                CodeBlock.Blank(isActive = true, indentLevel = 1, suggestions = listOf(Suggestion.Print))
            )
        )

        assertEquals(expectedState, state)
        assertContainsDecreaseIndentLevelAnalyticEvent(actions)
    }

    private fun assertContainsDecreaseIndentLevelAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedDecreaseIndentLevelHyperskillAnalyticEvent
            }
        }
    }
}