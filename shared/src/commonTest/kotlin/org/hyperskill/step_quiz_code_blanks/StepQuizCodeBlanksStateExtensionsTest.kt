package org.hyperskill.step_quiz_code_blanks

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.activeCodeBlockIndex
import org.hyperskill.app.step_quiz_code_blanks.presentation.createReply
import org.hyperskill.app.step_quiz_code_blanks.presentation.isVariableSuggestionsAvailable
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksStateExtensionsTest {
    @Test
    fun `activeCodeBlockIndex should return null if no active code block`() {
        val state = stubContentState(
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
        val state = stubContentState(
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
    fun `createReply should return Reply with code from code blocks and language from step options`() {
        val codeBlocks = listOf(
            CodeBlock.Print(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("\"test\"")
                    )
                )
            ),
            CodeBlock.Blank(isActive = true, suggestions = emptyList())
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

        val expectedReply = Reply.code(
            code = buildString {
                append("# solved with code blanks\n")
                append("print(\"test\")\n")
            },
            language = "python3"
        )

        assertEquals(expectedReply, state.createReply())
    }

    @Test
    fun `createReply should return correct Reply with Variable code block`() {
        val codeBlocks = listOf(
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("a")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("1")
                    )
                )
            ),
            CodeBlock.Print(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("a")
                    )
                )
            ),
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

        val expectedReply = Reply.code(
            code = buildString {
                append("# solved with code blanks\n")
                append("a = 1\nprint(a)")
            },
            language = "python3"
        )

        assertEquals(expectedReply, state.createReply())
    }

    @Test
    fun `createReply should return correct Reply with math expressions in Print`() {
        val codeBlocks = listOf(
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("x")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("1000")
                    )
                )
            ),
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("r")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("5")
                    )
                )
            ),
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("y")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("10")
                    )
                )
            ),
            CodeBlock.Print(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("x")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("*")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("(")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("1")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("+")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("r")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("/")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("100")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString(")")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("**")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("y")
                    )
                )
            )
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

        val expectedReply = Reply.code(
            code = buildString {
                append("# solved with code blanks\n")
                append("x = 1000\n")
                append("r = 5\n")
                append("y = 10\n")
                append("print(x * (1 + r / 100) ** y)")
            },
            language = "python3"
        )

        assertEquals(expectedReply, state.createReply())
    }

    @Test
    fun `createReply should return correct Reply with math expressions in Variable`() {
        val codeBlocks = listOf(
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("x")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("1000")
                    )
                )
            ),
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("r")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("5")
                    )
                )
            ),
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("y")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("10")
                    )
                )
            ),
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("a")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("x")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("*")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("(")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("1")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("+")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("r")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("/")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("100")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString(")")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("**")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("y")
                    )
                )
            ),
            CodeBlock.Print(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("a")
                    )
                )
            )
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

        val expectedReply = Reply.code(
            code = buildString {
                append("# solved with code blanks\n")
                append("x = 1000\n")
                append("r = 5\n")
                append("y = 10\n")
                append("a = x * (1 + r / 100) ** y\n")
                append("print(a)")
            },
            language = "python3"
        )

        assertEquals(expectedReply, state.createReply())
    }

    @Test
    fun `isVariableSuggestionsAvailable should return true if variable suggestions are available`() {
        val step = Step.stub(
            id = 1,
            block = Block.stub(options = Block.Options(codeBlanksVariables = listOf("a", "b")))
        )
        val state = stubContentState(
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
            val state = stubContentState(
                step = step,
                codeBlocks = emptyList()
            )

            assertFalse(state.isVariableSuggestionsAvailable)
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