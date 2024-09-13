package org.hyperskill.step_quiz_code_blanks.template

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.domain.model.template.CodeBlanksTemplateMapper
import org.hyperskill.app.step_quiz_code_blanks.domain.model.template.CodeBlockTemplateEntry
import org.hyperskill.app.step_quiz_code_blanks.domain.model.template.CodeBlockTemplateEntryType
import org.hyperskill.step.domain.model.stub

class CodeBlanksTemplateMapperTest {
    @Test
    fun `map should return math expression code blocks when step ID matches math expression template ID`() {
        val step = Step.stub(id = 47580) // Math expressions template step ID

        val result = CodeBlanksTemplateMapper.map(step)

        assertEquals(4, result.size)
        assertTrue(result[0] is CodeBlock.Variable)
        assertTrue(result[1] is CodeBlock.Variable)
        assertTrue(result[2] is CodeBlock.Variable)
        assertTrue(result[3] is CodeBlock.Blank)
    }

    @Test
    fun `map should return empty list when code blanks template contains unknown type`() {
        val step = Step.stub(
            id = 1,
            block = Block.stub(
                options = Block.Options(
                    codeBlanksTemplate = listOf(
                        CodeBlockTemplateEntry(type = CodeBlockTemplateEntryType.UNKNOWN)
                    )
                )
            )
        )

        val result = CodeBlanksTemplateMapper.map(step)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `map should return parsed code blocks when code blanks template is available`() {
        val codeBlanksTemplate = listOf(
            CodeBlockTemplateEntry(
                type = CodeBlockTemplateEntryType.VARIABLE,
                indentLevel = 0,
                isActive = false,
                isDeleteForbidden = true,
                children = listOf("x", "1000")
            ),
            CodeBlockTemplateEntry(
                type = CodeBlockTemplateEntryType.PRINT,
                indentLevel = 0,
                isActive = true,
                isDeleteForbidden = false,
                children = emptyList()
            )
        )
        val step = Step.stub(
            id = 1,
            block = Block.stub(options = Block.Options(codeBlanksTemplate = codeBlanksTemplate))
        )

        val expectedCodeBlocks = listOf(
            CodeBlock.Variable(
                indentLevel = 0,
                isDeleteForbidden = true,
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
            CodeBlock.Print(
                indentLevel = 0,
                isDeleteForbidden = false,
                children = listOf(
                    CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = emptyList(),
                        selectedSuggestion = null
                    )
                )
            )
        )

        val actualCodeBlocks = CodeBlanksTemplateMapper.map(step)
        assertEquals(expectedCodeBlocks, actualCodeBlocks)
    }
}