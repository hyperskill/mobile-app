package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedSpaceHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksReducerSpaceButtonClickedTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `SpaceButtonClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.SpaceButtonClicked)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `SpaceButtonClicked should not update state if no active code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
                        )
                    )
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.SpaceButtonClicked)

        assertEquals(initialState, state)
        assertContainsSpaceButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `SpaceButtonClicked should not update state if active code block is Blank`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = emptyList()
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.SpaceButtonClicked)

        assertEquals(initialState, state)
        assertContainsSpaceButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `SpaceButtonClicked should not update state if active code block is ElseStatement`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(CodeBlock.ElseStatement(isActive = true))
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.SpaceButtonClicked)

        assertEquals(initialState, state)
        assertContainsSpaceButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `SpaceButtonClicked should add a new child to active Print code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(Suggestion.ConstantString("suggestion")),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
                        )
                    )
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.SpaceButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(Suggestion.ConstantString("suggestion")),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
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

        assertEquals(expectedState, state)
        assertContainsSpaceButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `SpaceButtonClicked should add a new child to active Variable code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(Suggestion.ConstantString("x")),
                            selectedSuggestion = Suggestion.ConstantString("x")
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(Suggestion.ConstantString("suggestion")),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
                        )
                    )
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.SpaceButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(Suggestion.ConstantString("x")),
                            selectedSuggestion = Suggestion.ConstantString("x")
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(Suggestion.ConstantString("suggestion")),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
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

        assertEquals(expectedState, state)
        assertContainsSpaceButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `SpaceButtonClicked should add a new child with operations suggestions after closing parentheses`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            step = Step.stub(
                id = 1,
                block = Block.stub(
                    options = Block.Options(codeBlanksOperations = listOf("*", "+"))
                )
            ),
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(Suggestion.ConstantString(")")),
                            selectedSuggestion = Suggestion.ConstantString(")")
                        )
                    )
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.SpaceButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(Suggestion.ConstantString(")")),
                            selectedSuggestion = Suggestion.ConstantString(")")
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = initialState.codeBlanksOperationsSuggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsSpaceButtonClickedAnalyticEvent(actions)
    }

    private fun assertContainsSpaceButtonClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedSpaceHyperskillAnalyticEvent
            }
        }
    }
}