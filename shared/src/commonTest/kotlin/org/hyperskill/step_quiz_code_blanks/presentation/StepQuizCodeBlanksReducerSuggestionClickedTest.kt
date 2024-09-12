package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer

class StepQuizCodeBlanksReducerSuggestionClickedTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `SuggestionClicked should not update state if no active code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = false,
                    suggestions = emptyList()
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Print)
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should not update state if suggestion does not exist`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = emptyList()
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.ConstantString("test"))
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Print)
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `SuggestionClicked should update active Blank code block to Print if suggestion exists`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = listOf(Suggestion.Print)
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Print)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = initialState.codeBlanksStringsSuggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update active Blank code block to Variable if suggestion exists`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = listOf(Suggestion.Print, Suggestion.Variable)
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Variable)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = initialState.codeBlanksVariablesSuggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = initialState.codeBlanksStringsSuggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update Print code block with selected suggestion`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, actions) = reducer.reduce(initialState, message)

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(suggestion, (state.codeBlocks[0] as CodeBlock.Print).children[0].selectedSuggestion)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update Variable code block with selected suggestion for name`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = suggestion
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update Variable code block with selected suggestion for value`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = suggestion
                        )
                    )
                )
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    private fun assertContainsSuggestionClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
            }
        }
    }
}