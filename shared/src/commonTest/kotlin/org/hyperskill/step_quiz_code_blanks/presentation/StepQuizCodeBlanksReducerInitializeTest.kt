package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksReducerInitializeTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `Initialize should return Content state with active Blank and Print and Variable and If suggestions`() {
        val step = Step.stub(
            id = 1,
            block = Block.stub(options = Block.Options(codeBlanksVariables = listOf("a", "b")))
        )

        val message = StepQuizCodeBlanksFeature.InternalMessage.Initialize(step)
        val (state, actions) = reducer.reduce(StepQuizCodeBlanksFeature.State.Idle, message)

        val expectedState = StepQuizCodeBlanksFeature.State.Content(
            step = step,
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = listOf(Suggestion.Print, Suggestion.Variable, Suggestion.IfStatement)
                )
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `Initialize should return Content state with active Blank and Print suggestion`() {
        val step = Step.stub(id = 1)

        val message = StepQuizCodeBlanksFeature.InternalMessage.Initialize(step)
        val (state, actions) = reducer.reduce(StepQuizCodeBlanksFeature.State.Idle, message)

        val expectedState = StepQuizCodeBlanksFeature.State.Content(
            step = step,
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)))
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertTrue(actions.isEmpty())
    }
}