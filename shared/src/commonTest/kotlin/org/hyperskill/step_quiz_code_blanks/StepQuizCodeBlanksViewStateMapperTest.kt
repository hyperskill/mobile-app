package org.hyperskill.step_quiz_code_blanks

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
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
    fun `Content with print suggestion and disabled delete button when active code block is Blank`() {
        val state = stubState(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)))
        )
        val expectedViewState = StepQuizCodeBlanksViewState.Content(
            codeBlocks = listOf(StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = true)),
            suggestions = listOf(Suggestion.Print),
            isDeleteButtonEnabled = false,
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
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
            isSpaceButtonHidden = true
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
            isSpaceButtonHidden = true
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
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with active Variable and disabled delete button`() {
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
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with active not filled Variable and enabled delete button`() {
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
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with active filled Variable and enabled delete button`() {
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
                            isActive = true,
                            value = suggestions[0].text
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = false,
                            value = suggestions[1].text
                        )
                    )
                )
            ),
            suggestions = emptyList(),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true
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
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with suggestions when active code block is Print and no selected suggestion`() {
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
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with no suggestions when active code block is Print and has selected suggestion`() {
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
                            selectedSuggestion = suggestions[0]
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
                            value = suggestions[0].text
                        )
                    )
                )
            ),
            suggestions = emptyList(),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
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
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
    }

    @Test
    fun `Content with no suggestions when active code block is Variable and active child has selected suggestion`() {
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
                            isActive = true,
                            value = suggestions[0].text
                        ),
                        StepQuizCodeBlanksViewState.CodeBlockChildItem(
                            id = 1,
                            isActive = false,
                            value = suggestions[1].text
                        )
                    )
                )
            ),
            suggestions = emptyList(),
            isDeleteButtonEnabled = true,
            isSpaceButtonHidden = true
        )

        val actualViewState = StepQuizCodeBlanksViewStateMapper.map(state)

        assertEquals(expectedViewState, actualViewState)
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
        codeBlocks: List<CodeBlock>,
        onboardingState: OnboardingState = OnboardingState.Unavailable
    ): StepQuizCodeBlanksFeature.State.Content =
        StepQuizCodeBlanksFeature.State.Content(
            step = Step.stub(id = 0),
            codeBlocks = codeBlocks,
            onboardingState = onboardingState
        )
}