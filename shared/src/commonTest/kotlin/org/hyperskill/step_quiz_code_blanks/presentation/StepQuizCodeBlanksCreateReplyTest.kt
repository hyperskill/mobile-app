package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.createReply
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksCreateReplyTest {
    companion object {
        private const val REPLY_CODE_LANGUAGE = "python3"
        private const val REPLY_CODE_PREFIX = "# solved with code blanks\n"
    }

    private val step = Step.stub(
        id = 1,
        block = Block.stub(
            options = Block.Options(
                codeTemplates = mapOf(REPLY_CODE_LANGUAGE to "# put your python code here")
            )
        )
    )

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
        val state = StepQuizCodeBlanksFeature.State.Content.stub(step = step, codeBlocks = codeBlocks)

        val expectedReply = Reply.code(
            code = buildString {
                append(REPLY_CODE_PREFIX)
                append("print(\"test\")\n")
            },
            language = REPLY_CODE_LANGUAGE
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
        val state = StepQuizCodeBlanksFeature.State.Content.stub(step = step, codeBlocks = codeBlocks)

        val expectedReply = Reply.code(
            code = buildString {
                append(REPLY_CODE_PREFIX)
                append("a = 1\nprint(a)")
            },
            language = REPLY_CODE_LANGUAGE
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
        val state = StepQuizCodeBlanksFeature.State.Content.stub(step = step, codeBlocks = codeBlocks)

        val expectedReply = Reply.code(
            code = buildString {
                append(REPLY_CODE_PREFIX)
                append("x = 1000\n")
                append("r = 5\n")
                append("y = 10\n")
                append("print(x * (1 + r / 100) ** y)")
            },
            language = REPLY_CODE_LANGUAGE
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
        val state = StepQuizCodeBlanksFeature.State.Content.stub(step = step, codeBlocks = codeBlocks)

        val expectedReply = Reply.code(
            code = buildString {
                append(REPLY_CODE_PREFIX)
                append("x = 1000\n")
                append("r = 5\n")
                append("y = 10\n")
                append("a = x * (1 + r / 100) ** y\n")
                append("print(a)")
            },
            language = REPLY_CODE_LANGUAGE
        )

        assertEquals(expectedReply, state.createReply())
    }

    @Test
    fun `createReply should return correct Reply with single IfStatement`() {
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
                        selectedSuggestion = Suggestion.ConstantString("33")
                    )
                )
            ),
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("b")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("200")
                    )
                )
            ),
            CodeBlock.IfStatement(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("b")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString(">")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("a")
                    )
                )
            ),
            CodeBlock.Print(
                indentLevel = 1,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("\"b is greater than a\"")
                    )
                )
            )
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(step = step, codeBlocks = codeBlocks)

        val expectedReply = Reply.code(
            code = buildString {
                append(REPLY_CODE_PREFIX)
                append("a = 33\n")
                append("b = 200\n")
                append("if b > a:\n")
                append("\tprint(\"b is greater than a\")")
            },
            language = REPLY_CODE_LANGUAGE
        )

        assertEquals(expectedReply, state.createReply())
    }

    @Test
    fun `createReply should return correct Reply with multiple IfStatement and indentation level of 2`() {
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
                        selectedSuggestion = Suggestion.ConstantString("10")
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
                        selectedSuggestion = Suggestion.ConstantString("5")
                    )
                )
            ),
            CodeBlock.Variable(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("z")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("15")
                    )
                )
            ),
            CodeBlock.IfStatement(
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("x")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString(">")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("y")
                    )
                )
            ),
            CodeBlock.Print(
                indentLevel = 1,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("\"x is greater than y\"")
                    )
                )
            ),
            CodeBlock.IfStatement(
                indentLevel = 1,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("z")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString(">")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("x")
                    )
                )
            ),
            CodeBlock.Print(
                indentLevel = 2,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("\"z is greater than x\"")
                    )
                )
            ),
            CodeBlock.IfStatement(
                indentLevel = 1,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("z")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("<")
                    ),
                    CodeBlockChild.SelectSuggestion(
                        isActive = false,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("x")
                    )
                )
            ),
            CodeBlock.Print(
                indentLevel = 2,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = Suggestion.ConstantString("\"z is less than x\"")
                    )
                )
            )
        )
        val state = StepQuizCodeBlanksFeature.State.Content.stub(step = step, codeBlocks = codeBlocks)

        val expectedReply = Reply.code(
            code = buildString {
                append(REPLY_CODE_PREFIX)
                append("x = 10\n")
                append("y = 5\n")
                append("z = 15\n")
                append("if x > y:\n")
                append("\tprint(\"x is greater than y\")\n")
                append("\tif z > x:\n")
                append("\t\tprint(\"z is greater than x\")\n")
                append("\tif z < x:\n")
                append("\t\tprint(\"z is less than x\")")
            },
            language = REPLY_CODE_LANGUAGE
        )

        assertEquals(expectedReply, state.createReply())
    }
}