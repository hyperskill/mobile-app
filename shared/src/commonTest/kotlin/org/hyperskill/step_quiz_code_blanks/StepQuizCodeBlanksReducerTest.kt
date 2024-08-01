package org.hyperskill.step_quiz_code_blanks

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksReducerTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `Initialize should return Content state with active Blank code block`() {
        val step = Step.stub(id = 1)

        val message = StepQuizCodeBlanksFeature.InternalMessage.Initialize(step)
        val (state, actions) = reducer.reduce(StepQuizCodeBlanksFeature.State.Idle, message)

        val expectedState = StepQuizCodeBlanksFeature.State.Content(
            step = step,
            codeBlocks = listOf(CodeBlock.Blank(isActive = true))
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `SuggestionClicked should not update state if no active code block`() {
        val initialState = stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = false)))

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Print)
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should not update state if suggestion does not exist`() {
        val initialState = stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = true)))

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
        val initialState = stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = true)))

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Print)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = true,
                    suggestions = initialState.codeBlanksStringsSuggestions,
                    selectedSuggestion = null
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update Print code block with selected suggestion`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = true,
                    suggestions = listOf(suggestion),
                    selectedSuggestion = null
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, actions) = reducer.reduce(initialState, message)

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(suggestion, (state.codeBlocks[0] as CodeBlock.Print).selectedSuggestion)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

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
    fun `CodeBlockClicked should update active code block`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false),
                CodeBlock.Print(isActive = true, suggestions = emptyList(), selectedSuggestion = null)
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = false)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = true),
                CodeBlock.Print(isActive = false, suggestions = emptyList(), selectedSuggestion = null)
            )
        )

        assertEquals(expectedState, state)
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
            }
        }
    }

    @Test
    fun `DeleteButtonClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `DeleteButtonClicked should log analytic event and not update state if no active code block`() {
        val initialState = stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = false)))

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should not update state if active code block is Blank`() {
        val initialState = stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = true)))

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should update state if active Print code block has selected suggestion`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = true,
                    suggestions = listOf(suggestion),
                    selectedSuggestion = suggestion
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = true,
                    suggestions = listOf(suggestion),
                    selectedSuggestion = null
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should set next code block as active if no code block before deleted`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = true,
                    suggestions = listOf(Suggestion.ConstantString("suggestion")),
                    selectedSuggestion = null
                ),
                CodeBlock.Blank(isActive = false)
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true))
        )

        assertEquals(expectedState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should set previous code block as active if active Print code block is deleted`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false),
                CodeBlock.Print(
                    isActive = true,
                    suggestions = listOf(Suggestion.ConstantString("suggestion")),
                    selectedSuggestion = null
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true))
        )

        assertEquals(expectedState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should not update state if no active code block`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = false,
                    suggestions = listOf(Suggestion.ConstantString("suggestion")),
                    selectedSuggestion = null
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should replace single Print code block with Blank`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    isActive = true,
                    suggestions = emptyList(),
                    selectedSuggestion = null
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true))
        )

        assertEquals(expectedState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `EnterButtonClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `EnterButtonClicked should log analytic event and not update state if no active code block`() {
        val initialState = stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = false)))

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        assertEquals(initialState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `EnterButtonClicked should log analytic event and add new active Blank block if active code block exists`() {
        val initialState = stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = true)))

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false),
                CodeBlock.Blank(isActive = true)
            )
        )

        assertEquals(expectedState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `EnterButtonClicked should add new active Blank block after active code block`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = true),
                CodeBlock.Print(
                    isActive = false,
                    suggestions = listOf(Suggestion.ConstantString("suggestion")),
                    selectedSuggestion = null
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false),
                CodeBlock.Blank(isActive = true),
                CodeBlock.Print(
                    isActive = false,
                    suggestions = listOf(Suggestion.ConstantString("suggestion")),
                    selectedSuggestion = null
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    private fun assertContainsSuggestionClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
            }
        }
    }

    private fun assertContainsDeleteButtonClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
            }
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

    private fun stubContentState(
        step: Step = Step.stub(id = 1),
        codeBlocks: List<CodeBlock>
    ): StepQuizCodeBlanksFeature.State.Content =
        StepQuizCodeBlanksFeature.State.Content(
            step = step,
            codeBlocks = codeBlocks
        )
}