package org.hyperskill.step_quiz_code_blanks

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.activeCodeBlockIndex
import org.hyperskill.app.step_quiz_code_blanks.presentation.createReply
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksStateExtensionsTest {
    @Test
    fun `activeCodeBlockIndex should return null if no active code block`() {
        val state = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false),
                CodeBlock.Print(
                    isActive = false,
                    suggestions = emptyList(),
                    selectedSuggestion = null
                )
            )
        )
        assertNull(state.activeCodeBlockIndex)
    }

    @Test
    fun `activeCodeBlockIndex should return index of the active code block`() {
        val state = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false),
                CodeBlock.Print(
                    isActive = true,
                    suggestions = emptyList(),
                    selectedSuggestion = null
                )
            )
        )
        assertEquals(1, state.activeCodeBlockIndex)
    }

    @Test
    fun `createReply should return Reply with code from code blocks and language from step options`() {
        val codeBlocks = listOf(
            CodeBlock.Print(
                isActive = false,
                suggestions = emptyList(),
                selectedSuggestion = Suggestion.ConstantString("\"test\"")
            ),
            CodeBlock.Blank(isActive = true)
        )
        val step = Step.stub(id = 1).copy(
            block = Block.stub(
                options = Block.Options(
                    codeTemplates = mapOf("python3" to "# put your python code here")
                )
            )
        )
        val state = stubContentState(
            step = step,
            codeBlocks = codeBlocks
        )

        val expectedReply = Reply.code(code = "print(\"test\")\n", language = "python3")

        assertEquals(expectedReply, state.createReply())
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