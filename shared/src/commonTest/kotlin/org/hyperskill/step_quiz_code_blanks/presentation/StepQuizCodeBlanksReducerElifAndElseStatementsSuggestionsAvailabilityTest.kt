package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer

class StepQuizCodeBlanksReducerElifAndElseStatementsSuggestionsAvailabilityTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `Should return false if index is less than 2`() {
        val result =
            reducer.areElifAndElseStatementsSuggestionsAvailable(index = 1, indentLevel = 0, codeBlocks = emptyList())
        assertFalse(result)
    }

    @Test
    fun `Should return false if codeBlocks is empty`() {
        val result =
            reducer.areElifAndElseStatementsSuggestionsAvailable(index = 2, indentLevel = 0, codeBlocks = emptyList())
        assertFalse(result)
    }

    @Test
    fun `Should return false if no previous code block at same indent level`() {
        val codeBlocks = listOf(
            CodeBlock.IfStatement(indentLevel = 0, children = emptyList()),
            CodeBlock.Print(indentLevel = 1, children = emptyList())
        )
        val result =
            reducer.areElifAndElseStatementsSuggestionsAvailable(index = 2, indentLevel = 1, codeBlocks = codeBlocks)
        assertFalse(result)
    }

    @Test
    fun `Should return true if previous code block is IfStatement at same indent level`() {
        val codeBlocks = listOf(
            CodeBlock.IfStatement(indentLevel = 0, children = emptyList()),
            CodeBlock.Print(indentLevel = 1, children = emptyList())
        )
        val result =
            reducer.areElifAndElseStatementsSuggestionsAvailable(index = 2, indentLevel = 0, codeBlocks = codeBlocks)
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
        val result =
            reducer.areElifAndElseStatementsSuggestionsAvailable(index = 4, indentLevel = 1, codeBlocks = codeBlocks)
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
        val result =
            reducer.areElifAndElseStatementsSuggestionsAvailable(index = 4, indentLevel = 0, codeBlocks = codeBlocks)
        assertTrue(result)
    }
}