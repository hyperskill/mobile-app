package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer

class StepQuizCodeBlanksReducerDeleteButtonClickedTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `DeleteButtonClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `DeleteButtonClicked should not update state if no active code block`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
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

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should not update state if active code block is Blank and single`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = emptyList()
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should clear suggestion if active Print code block has selected suggestion`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = suggestion
                        )
                    )
                )
            )
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should set next code block as active if no code block before deleted`() {
        val initialStates = listOf(
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Print(
                        children = listOf(
                            CodeBlockChild.SelectSuggestion(
                                isActive = true,
                                suggestions = emptyList(),
                                selectedSuggestion = null
                            )
                        )
                    ),
                    CodeBlock.Blank(isActive = false, suggestions = emptyList())
                )
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Blank(isActive = true, suggestions = emptyList()),
                    CodeBlock.Blank(isActive = false, suggestions = emptyList())
                )
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Blank(isActive = true, suggestions = emptyList()),
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
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Print(
                        children = listOf(
                            CodeBlockChild.SelectSuggestion(
                                isActive = true,
                                suggestions = emptyList(),
                                selectedSuggestion = null
                            )
                        )
                    ),
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
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
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
                    ),
                    CodeBlock.Blank(isActive = false, suggestions = emptyList())
                )
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Blank(isActive = true, suggestions = emptyList()),
                    CodeBlock.Variable(
                        children = listOf(
                            CodeBlockChild.SelectSuggestion(
                                isActive = false,
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
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Variable(
                        children = listOf(
                            CodeBlockChild.SelectSuggestion(
                                isActive = false,
                                suggestions = emptyList(),
                                selectedSuggestion = null
                            ),
                            CodeBlockChild.SelectSuggestion(
                                isActive = true,
                                suggestions = emptyList(),
                                selectedSuggestion = null
                            )
                        )
                    ),
                    CodeBlock.Variable(
                        children = listOf(
                            CodeBlockChild.SelectSuggestion(
                                isActive = false,
                                suggestions = emptyList(),
                                selectedSuggestion = null
                            ),
                            CodeBlockChild.SelectSuggestion(
                                isActive = false,
                                suggestions = emptyList(),
                                selectedSuggestion = Suggestion.ConstantString("suggestion")
                            )
                        )
                    )
                )
            )
        )
        val expectedStates = listOf(
            initialStates[0].copy(codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList()))),
            initialStates[1].copy(codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList()))),
            initialStates[2].copy(
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
            ),
            initialStates[3].copy(
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
            ),
            initialStates[4].copy(codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList()))),
            initialStates[5].copy(
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
            ),
            initialStates[6].copy(
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
                                selectedSuggestion = Suggestion.ConstantString("suggestion")
                            )
                        )
                    )
                )
            )
        )

        initialStates.zip(expectedStates).forEach { (initialState, expectedState) ->
            val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)
            assertEquals(expectedState, state)
            assertContainsDeleteButtonClickedAnalyticEvent(actions)
        }
    }

    @Test
    fun `DeleteButtonClicked should set previous code block as active if has code block before deleted`() {
        val initialStates = listOf(
            StepQuizCodeBlanksFeature.State.Content.stub(
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
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Print(
                        children = listOf(
                            CodeBlockChild.SelectSuggestion(
                                isActive = false,
                                suggestions = emptyList(),
                                selectedSuggestion = null
                            )
                        )
                    ),
                    CodeBlock.Blank(isActive = true, suggestions = emptyList())
                )
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Print(
                        children = listOf(
                            CodeBlockChild.SelectSuggestion(
                                isActive = false,
                                suggestions = listOf(Suggestion.ConstantString("suggestion")),
                                selectedSuggestion = Suggestion.ConstantString("suggestion")
                            )
                        )
                    ),
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
            ),
            StepQuizCodeBlanksFeature.State.Content.stub(
                codeBlocks = listOf(
                    CodeBlock.Blank(isActive = false, suggestions = emptyList()),
                    CodeBlock.Blank(isActive = true, suggestions = emptyList())
                )
            )
        )
        val expectedStates = listOf(
            initialStates[0].copy(codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList()))),
            initialStates[1].copy(
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
            ),
            initialStates[2].copy(
                codeBlocks = listOf(
                    CodeBlock.Print(
                        children = listOf(
                            CodeBlockChild.SelectSuggestion(
                                isActive = true,
                                suggestions = listOf(Suggestion.ConstantString("suggestion")),
                                selectedSuggestion = Suggestion.ConstantString("suggestion")
                            )
                        )
                    )
                )
            ),
            initialStates[0].copy(codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList()))),
        )

        initialStates.zip(expectedStates).forEach { (initialState, expectedState) ->
            val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)
            assertEquals(expectedState, state)
            assertContainsDeleteButtonClickedAnalyticEvent(actions)
        }
    }

    @Test
    fun `DeleteButtonClicked should replace single Print code block with Blank`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
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

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)))
        )

        assertEquals(expectedState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should replace single Variable code block with Blank`() {
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
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

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)))
        )

        assertEquals(expectedState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    private fun assertContainsDeleteButtonClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
            }
        }
    }
}