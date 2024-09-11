package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.activeCodeBlockIndex
import org.hyperskill.app.step_quiz_code_blanks.presentation.isVariableSuggestionsAvailable
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksStateExtensionsTest {
    @Test
    fun `activeCodeBlockIndex should return null if no active code block`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
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
        assertNull(state.activeCodeBlockIndex())
    }

    @Test
    fun `activeCodeBlockIndex should return index of the active code block`() {
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
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
        assertEquals(1, state.activeCodeBlockIndex())
    }

    @Test
    fun `isVariableSuggestionsAvailable should return true if variable suggestions are available`() {
        val step = Step.stub(
            id = 1,
            block = Block.stub(options = Block.Options(codeBlanksVariables = listOf("a", "b")))
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(
            step = step,
            codeBlocks = emptyList()
        )

        assertTrue(state.isVariableSuggestionsAvailable)
    }

    @Test
    fun `isVariableSuggestionsAvailable should return false if variable suggestions are not available`() {
        listOf(null, emptyList<String>()).forEach { codeBlanksVariables ->
            val step = Step.stub(
                id = 1,
                block = Block.stub(options = Block.Options(codeBlanksVariables = codeBlanksVariables))
            )
            val state = StepQuizCodeBlanksFeature.State.Content.stub(
                step = step,
                codeBlocks = emptyList()
            )

            assertFalse(state.isVariableSuggestionsAvailable)
        }
    }
}