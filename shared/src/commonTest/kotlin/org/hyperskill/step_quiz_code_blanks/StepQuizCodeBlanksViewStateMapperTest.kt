package org.hyperskill.step_quiz_code_blanks

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.app.step_quiz_code_blanks.view.mapper.StepQuizCodeBlanksViewStateMapper
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksViewStateMapperTest {
    @Test
    fun `map should return Idle view state for Idle state`() {
        val state = StepQuizCodeBlanksFeature.State.Idle
        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)
        assertEquals(StepQuizCodeBlanksViewState.Idle, viewState)
    }

    @Test
    fun `Content with suggestions and enabled delete button when active code block is Print`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with sequence of filled Print and active Blank`() {
        val printSuggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = printSuggestions,
                            selectedSuggestion = printSuggestions[0]
                        )
                    )
                ),
                CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print))
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = printSuggestions[0].text
                        )
                    )
                ),
                StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 1, isActive = true)
            ),
            suggestions = listOf(Suggestion.Print),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with sequence of filled Print and active not filled Print`() {
        val printSuggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = printSuggestions,
                            selectedSuggestion = printSuggestions[0]
                        )
                    )
                ),
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = printSuggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = printSuggestions[0].text
                        )
                    )
                ),
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 1,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = printSuggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be disabled when active code block is Blank and single`() {
        val state = stubState(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)))
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = true)),
            suggestions = listOf(Suggestion.Print),
            isDeleteButtonEnabled = false,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when active code block is Print and single`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(Suggestion.Print),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = listOf(Suggestion.Print),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when Variable active name is unselected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = false,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when Variable active name is selected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = suggestions[0].text
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = false,
                            value = null
                        )
                    )
                )
            ),
            suggestions = emptyList(),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when Variable active value child index is greater than one`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[1]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = suggestions[0].text
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = false,
                            value = suggestions[1].text
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 2,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be disabled when Variable name is selected and active value is unselected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = suggestions[0].text
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = false,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when Variable name is selected and active value is selected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[1]
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = suggestions[0].text
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = true,
                            value = suggestions[1].text
                        )
                    )
                )
            ),
            suggestions = emptyList(),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = false,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when Variable name is unselected and active value is unselected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = null
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when Variable name is unselected and active value is selected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = null
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = true,
                            value = suggestions[0].text
                        )
                    )
                )
            ),
            suggestions = emptyList(),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = false,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when IfStatement active child index greater than zero`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.IfStatement(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = false,
                            value = suggestions[0].text
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Delete button should be enabled when IfStatement child is selected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = suggestions[0]
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.IfStatement(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = suggestions[0].text
                        )
                    )
                )
            ),
            suggestions = emptyList(),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = false,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    /* ktlint-disable */
    @Test
    fun `Delete button should be enabled when IfStatement child is unselected and next code block on same indent level`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    indentLevel = 1,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Print(
                    indentLevel = 1,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.IfStatement(
                    id = 0,
                    indentLevel = 1,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                ),
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 1,
                    indentLevel = 1,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = false
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    /* ktlint-disable */
    @Test
    fun `Delete button should be disabled when IfStatement child is unselected and next code block on different indent level`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    indentLevel = 1,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Print(
                    indentLevel = 2,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.IfStatement(
                    id = 0,
                    indentLevel = 1,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                ),
                StepQuizCodeBlanksViewState.CodeBlockItem.Print(
                    id = 1,
                    indentLevel = 2,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = false,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = false
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with suggestions when active code block is Blank`() {
        val suggestions = listOf(Suggestion.Print, Suggestion.Variable)
        val state = stubState(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = suggestions))
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = true)),
            suggestions = suggestions,
            isDeleteButtonEnabled = false,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content without suggestions when code block active child has selected suggestion`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val children = listOf(
            CodeBlockChild.SelectSuggestion(
                isActive = true,
                suggestions = suggestions,
                selectedSuggestion = suggestions[0]
            )
        )
        val codeBlocks = listOf(
            CodeBlock.Print(children = children),
            CodeBlock.Variable(children = children),
            CodeBlock.IfStatement(children = children)
        )

        codeBlocks.forEach { codeBlock ->
            val state = stubState(codeBlocks = listOf(codeBlock))
            val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

            assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
            assertTrue(viewState.suggestions.isEmpty())
        }
    }

    @Test
    fun `Content with suggestions when code block active child is unselected`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val children = listOf(
            CodeBlockChild.SelectSuggestion(
                isActive = true,
                suggestions = suggestions,
                selectedSuggestion = null
            )
        )
        val codeBlocks = listOf(
            CodeBlock.Print(children = children),
            CodeBlock.Variable(children = children),
            CodeBlock.IfStatement(children = children)
        )

        codeBlocks.forEach { codeBlock ->
            val state = stubState(codeBlocks = listOf(codeBlock))
            val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

            assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
            assertEquals(suggestions, viewState.suggestions)
        }
    }

    @Test
    fun `Content with suggestions when active code block is Variable and active child has no selected suggestion`() {
        val suggestions = listOf(
            Suggestion.ConstantString("1"),
            Suggestion.ConstantString("2")
        )
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = suggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(
                StepQuizCodeBlanksViewState.CodeBlockItem.Variable(
                    id = 0,
                    children = listOf(
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 0,
                            isActive = true,
                            value = null
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = false,
                            value = null
                        )
                    )
                )
            ),
            suggestions = suggestions,
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true,
            isDecreaseIndentLevelButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `isSpaceButtonHidden should be true when codeBlanksOperationsSuggestions is empty`() {
        val state = stubState(
            step = Step.stub(id = 0),
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when no active code block`() {
        val state = stubState(codeBlocks = listOf(CodeBlock.Blank(isActive = false, suggestions = emptyList())))

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when active Print code block has no active child`() {
        val state = stubState(
            codeBlocks = listOf(
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

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when active Print code block child has no selected suggestion`() {
        val state = stubState(
            codeBlocks = listOf(
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

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be false when active Print code block child has selected suggestion`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("suggestion")
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isSpaceButtonHidden)
    }

    /* ktlint-disable */
    @Test
    fun `isSpaceButtonHidden should be true when active Variable code block's first child has no selected suggestion`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = emptyList(),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when active Variable code block's second child has no selected suggestion`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("x")
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be false when active IfStatement code block child has selected suggestion`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = emptyList(),
                            selectedSuggestion = Suggestion.ConstantString("if")
                        )
                    )
                )
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isSpaceButtonHidden should be true when active IfStatement code block child has no selected suggestion`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.IfStatement(
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

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSpaceButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be true when no active code block`() {
        val state = stubState(codeBlocks = listOf(CodeBlock.Blank(isActive = false, suggestions = emptyList())))

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be true when active code block's indent level is less than 1`() {
        val state = stubState(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, indentLevel = 0, suggestions = emptyList()))
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be false when active code block's indent level is 1 or more`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    indentLevel = 1,
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

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be true when previous code block is IfStatement`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.IfStatement(indentLevel = 1, children = emptyList()),
                CodeBlock.Blank(isActive = true, indentLevel = 1, suggestions = emptyList())
            )
        )

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `isDecreaseIndentLevelButtonHidden should be false when previous code block is not IfStatement`() {
        val state = stubState(
            codeBlocks = listOf(
                CodeBlock.Print(indentLevel = 1, children = emptyList()),
                CodeBlock.Print(
                    indentLevel = 1,
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

        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isDecreaseIndentLevelButtonHidden)
    }

    @Test
    fun `Action buttons hidden when onboarding is available`() {
        val state = stubState(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList())),
            onboardingState = OnboardingState.HighlightSuggestions
        )
        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isActionButtonsHidden)
    }

    @Test
    fun `Action buttons not hidden when onboarding is unavailable`() {
        val state = stubState(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList())),
            onboardingState = OnboardingState.Unavailable
        )
        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertFalse(viewState.isActionButtonsHidden)
    }

    @Test
    fun `Suggestions highlight effect is active when onboardingState is HighlightSuggestions`() {
        val state = stubState(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList())),
            onboardingState = OnboardingState.HighlightSuggestions
        )
        val viewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertTrue(viewState is StepQuizCodeBlanksViewState.Content)
        assertTrue(viewState.isSuggestionsHighlightEffectActive)
    }

    private fun stubState(
        step: Step = Step.stub(
            id = 0,
            block = Block.stub(
                options = Block.Options(
                    codeBlanksOperations = listOf("+")
                )
            )
        ),
        codeBlocks: List<CodeBlock>,
        onboardingState: OnboardingState = OnboardingState.Unavailable
    ): StepQuizCodeBlanksFeature.State.Content =
        StepQuizCodeBlanksFeature.State.Content(
            step = step,
            codeBlocks = codeBlocks,
            onboardingState = onboardingState
        )
}

// internal fun Step.codeBlanksOperationsSuggestions(): List<Suggestion.ConstantString> =
//    block.options.codeBlanksOperations.orEmpty().map(Suggestion::ConstantString)