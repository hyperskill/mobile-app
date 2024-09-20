package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksReducerInitializeTest {
    private val reducer = StepQuizCodeBlanksReducer.stub()

    @Test
    fun `Initialize should return Content state with active Blank and correct suggestions`() {
        val blockOptions = listOf(
            Block.Options(codeBlanksVariables = listOf("a", "b")),
            Block.Options(
                codeBlanksVariables = listOf("a", "b"),
                codeBlanksAvailableConditions = setOf("if", "elif", "else")
            )
        )
        val expectedSuggestions = listOf(
            listOf(Suggestion.Print, Suggestion.Variable),
            listOf(Suggestion.Print, Suggestion.Variable, Suggestion.IfStatement)
        )

        blockOptions.forEachIndexed { index, options ->
            val step = Step.stub(
                id = 1,
                block = Block.stub(options = options)
            )

            val message = StepQuizCodeBlanksFeature.InternalMessage.Initialize(step)
            val (state, actions) = reducer.reduce(StepQuizCodeBlanksFeature.State.Idle, message)

            val expectedState = StepQuizCodeBlanksFeature.State.Content(
                step = step,
                codeBlocks = listOf(
                    CodeBlock.Blank(
                        isActive = true,
                        suggestions = expectedSuggestions[index]
                    )
                )
            )

            assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
            assertEquals(expectedState.codeBlocks, state.codeBlocks)
            assertTrue(actions.isEmpty())
        }
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