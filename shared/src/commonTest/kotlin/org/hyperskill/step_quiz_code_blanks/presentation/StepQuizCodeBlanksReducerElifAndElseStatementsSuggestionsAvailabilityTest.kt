package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksResolver

class StepQuizCodeBlanksReducerElifAndElseStatementsSuggestionsAvailabilityTest {
    companion object {
        private val availableConditions = setOf("if", "elif", "else")
    }

    @Test
    fun `Should return false if index is less than 2`() {
        val result = StepQuizCodeBlanksResolver.areElifAndElseStatementsSuggestionsAvailable(
            index = 1,
            indentLevel = 0,
            codeBlocks = emptyList(),
            availableConditions = availableConditions
        )
        assertFalse(result)
    }

    @Test
    fun `Should return false if codeBlocks is empty`() {
        val result = StepQuizCodeBlanksResolver.areElifAndElseStatementsSuggestionsAvailable(
            index = 2,
            indentLevel = 0,
            codeBlocks = emptyList(),
            availableConditions = availableConditions
        )
        assertFalse(result)
    }

    @Test
    fun `Should return false if availableConditions is empty`() {
        val codeBlocks = listOf(
            CodeBlock.IfStatement(indentLevel = 0, children = emptyList()),
            CodeBlock.Print(indentLevel = 1, children = emptyList())
        )
        val result = StepQuizCodeBlanksResolver.areElifAndElseStatementsSuggestionsAvailable(
            index = 2,
            indentLevel = 0,
            codeBlocks = codeBlocks,
            availableConditions = emptySet()
        )
        assertFalse(result)
    }

    @Test
    fun `Should return false if no previous code block at same indent level`() {
        val codeBlocks = listOf(
            CodeBlock.IfStatement(indentLevel = 0, children = emptyList()),
            CodeBlock.Print(indentLevel = 1, children = emptyList())
        )
        val result = StepQuizCodeBlanksResolver.areElifAndElseStatementsSuggestionsAvailable(
            index = 2,
            indentLevel = 1,
            codeBlocks = codeBlocks,
            availableConditions = availableConditions
        )
        assertFalse(result)
    }

    @Test
    fun `Should return true if previous code block is IfStatement at same indent level`() {
        val codeBlocks = listOf(
            CodeBlock.IfStatement(indentLevel = 0, children = emptyList()),
            CodeBlock.Print(indentLevel = 1, children = emptyList())
        )
        val result = StepQuizCodeBlanksResolver.areElifAndElseStatementsSuggestionsAvailable(
            index = 2,
            indentLevel = 0,
            codeBlocks = codeBlocks,
            availableConditions = availableConditions
        )
        assertTrue(result)
    }

    @Test
    fun `Should return true if previous code block is IfStatement at same indent level nested`() {
        val codeBlocks = listOf(
            CodeBlock.IfStatement(indentLevel = 0, children = emptyList()),
            CodeBlock.Print(indentLevel = 1, children = emptyList()),
            CodeBlock.IfStatement(indentLevel = 1, children = emptyList()),
            CodeBlock.Print(indentLevel = 2, children = emptyList())
        )
        val result = StepQuizCodeBlanksResolver.areElifAndElseStatementsSuggestionsAvailable(
            index = 4,
            indentLevel = 1,
            codeBlocks = codeBlocks,
            availableConditions = availableConditions
        )
        assertTrue(result)
    }

    @Test
    fun `Should return true if previous code block is ElifStatement at same indent level`() {
        val codeBlocks = listOf(
            CodeBlock.IfStatement(indentLevel = 0, children = emptyList()),
            CodeBlock.Print(indentLevel = 1, children = emptyList()),
            CodeBlock.ElifStatement(indentLevel = 0, children = emptyList()),
            CodeBlock.Print(indentLevel = 1, children = emptyList())
        )
        val result = StepQuizCodeBlanksResolver.areElifAndElseStatementsSuggestionsAvailable(
            index = 4,
            indentLevel = 0,
            codeBlocks = codeBlocks,
            availableConditions = availableConditions
        )
        assertTrue(result)
    }
}