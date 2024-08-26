package org.hyperskill.step_quiz_code_blanks

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockChildHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksReducerTest {
    private val reducer = StepQuizCodeBlanksReducer(StepRoute.Learn.Step(1, null))

    @Test
    fun `Initialize should return Content state with active Blank and Print and Variable suggestions`() {
        val step = Step.stub(
            id = 1,
            block = Block.stub(options = Block.Options(codeBlanksVariables = listOf("a", "b")))
        )

        val message = StepQuizCodeBlanksFeature.InternalMessage.Initialize(step)
        val (state, actions) = reducer.reduce(StepQuizCodeBlanksFeature.State.Idle, message)

        val expectedState = StepQuizCodeBlanksFeature.State.Content(
            step = step,
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = listOf(Suggestion.Print, Suggestion.Variable)
                )
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `Initialize should return Content state with active Blank and Print suggestion`() {
        val step = Step.stub(id = 1)

        val message = StepQuizCodeBlanksFeature.InternalMessage.Initialize(step)
        val (state, actions) = reducer.reduce(StepQuizCodeBlanksFeature.State.Idle, message)

        val expectedState = StepQuizCodeBlanksFeature.State.Content(
            step = step,
            codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)))
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `SuggestionClicked should not update state if no active code block`() {
        val initialState =
            stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = false, suggestions = emptyList())))

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Print)
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should not update state if suggestion does not exist`() {
        val initialState =
            stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList())))

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.ConstantString("test"))
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Print)
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `SuggestionClicked should update active Blank code block to Print if suggestion exists`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = listOf(Suggestion.Print)
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Print)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = initialState.codeBlanksStringsSuggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update active Blank code block to Variable if suggestion exists`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Blank(
                    isActive = true,
                    suggestions = listOf(Suggestion.Print, Suggestion.Variable)
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(Suggestion.Variable)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = initialState.codeBlanksVariablesSuggestions,
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = initialState.codeBlanksStringsSuggestions,
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update Print code block with selected suggestion`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = stubContentState(
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

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, actions) = reducer.reduce(initialState, message)

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(suggestion, (state.codeBlocks[0] as CodeBlock.Print).children[0].selectedSuggestion)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update Variable code block with selected suggestion for name`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = suggestion
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `SuggestionClicked should update Variable code block with selected suggestion for value`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = listOf(suggestion),
                            selectedSuggestion = suggestion
                        )
                    )
                )
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(expectedState.codeBlocks, state.codeBlocks)
        assertContainsSuggestionClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = true)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `CodeBlockClicked should update active Print code block`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = false)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = true, suggestions = emptyList()),
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
            }
        }
    }

    @Test
    fun `CodeBlockClicked should update active Variable code block`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true, suggestions = emptyList(), selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Blank(id = 0, isActive = false)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Print(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = true, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                ),
                CodeBlock.Variable(
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false, suggestions = emptyList(), selectedSuggestion = null
                        )
                    )
                )
            )
        )

        assertEquals(expectedState, state)
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
            }
        }
    }

    @Test
    fun `CodeBlockChildClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val message = StepQuizCodeBlanksFeature.Message.CodeBlockChildClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Variable(id = 0, children = emptyList()),
            codeBlockChildItem = StepQuizCodeBlanksViewState.CodeBlockChildItem(id = 0, isActive = false, value = null)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `CodeBlockChildClicked should not update state if target code block is not found`() {
        val initialState = stubContentState(
            codeBlocks = listOf(
                CodeBlock.Variable(
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

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockChildClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Variable(id = 1, children = emptyList()),
            codeBlockChildItem = StepQuizCodeBlanksViewState.CodeBlockChildItem(id = 0, isActive = false, value = null)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContainsCodeBlockChildClickedAnalyticEvent(actions)
    }

    @Test
    fun `CodeBlockChildClicked should update state to activate the clicked child`() {
        val initialState = stubContentState(
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
                )
            )
        )

        val message = StepQuizCodeBlanksFeature.Message.CodeBlockChildClicked(
            codeBlockItem = StepQuizCodeBlanksViewState.CodeBlockItem.Variable(id = 0, children = emptyList()),
            codeBlockChildItem = StepQuizCodeBlanksViewState.CodeBlockChildItem(id = 0, isActive = false, value = null)
        )
        val (state, actions) = reducer.reduce(initialState, message)

        val expectedState = initialState.copy(
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

        assertEquals(expectedState, state)
        assertContainsCodeBlockChildClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `DeleteButtonClicked should log analytic event and not update state if no active code block`() {
        val initialState =
            stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = false, suggestions = emptyList())))

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should not update state if active code block is Blank and single`() {
        val initialState =
            stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList())))

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.DeleteButtonClicked)

        assertEquals(initialState, state)
        assertContainsDeleteButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `DeleteButtonClicked should clear suggestion if active Print code block has selected suggestion`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = stubContentState(
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
            stubContentState(
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
            stubContentState(
                codeBlocks = listOf(
                    CodeBlock.Blank(isActive = true, suggestions = emptyList()),
                    CodeBlock.Blank(isActive = false, suggestions = emptyList())
                )
            ),
            stubContentState(
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
            stubContentState(
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
            stubContentState(
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
            stubContentState(
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
            stubContentState(
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
            stubContentState(
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
            stubContentState(
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
            stubContentState(
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
            stubContentState(
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
    fun `DeleteButtonClicked should not update state if no active code block`() {
        val initialState = stubContentState(
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
    fun `DeleteButtonClicked should replace single Print code block with Blank`() {
        val initialState = stubContentState(
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
        val initialState = stubContentState(
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

    @Test
    fun `EnterButtonClicked should not update state if state is not Content`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        assertEquals(initialState, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `EnterButtonClicked should log analytic event and not update state if no active code block`() {
        val initialState =
            stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = false, suggestions = emptyList())))

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        assertEquals(initialState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `EnterButtonClicked should log analytic event and add new active Blank block if active code block exists`() {
        val initialState =
            stubContentState(codeBlocks = listOf(CodeBlock.Blank(isActive = true, suggestions = emptyList())))

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
                CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print))
            )
        )

        assertEquals(expectedState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `EnterButtonClicked should add new active Blank block after active code block`() {
        val initialState = stubContentState(
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
        )

        val (state, actions) = reducer.reduce(initialState, StepQuizCodeBlanksFeature.Message.EnterButtonClicked)

        val expectedState = initialState.copy(
            codeBlocks = listOf(
                CodeBlock.Blank(isActive = false, suggestions = emptyList()),
                CodeBlock.Blank(isActive = true, suggestions = listOf(Suggestion.Print)),
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

        assertEquals(expectedState, state)
        assertContainsEnterButtonClickedAnalyticEvent(actions)
    }

    @Test
    fun `Onboarding should be unavailable`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, _) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.InternalMessage.Initialize(Step.stub(id = 1))
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertTrue(state.onboardingState is OnboardingState.Unavailable)
    }

    @Test
    fun `Onboarding should be available`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, _) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.InternalMessage.Initialize(Step.stub(id = 47329))
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertTrue(state.onboardingState is OnboardingState.HighlightSuggestions)
    }

    @Test
    fun `Onboarding SuggestionClicked should update onboardingState to HighlightCallToActionButton`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = stubContentState(
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
            ),
            onboardingState = OnboardingState.HighlightSuggestions
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, _) = reducer.reduce(initialState, message)

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(OnboardingState.HighlightCallToActionButton, state.onboardingState)
    }

    private fun assertContainsSuggestionClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
            }
        }
    }

    private fun assertContainsCodeBlockChildClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedCodeBlockChildHyperskillAnalyticEvent
            }
        }
    }

    private fun assertContainsDeleteButtonClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
            }
        }
    }

    private fun assertContainsEnterButtonClickedAnalyticEvent(actions: Set<StepQuizCodeBlanksFeature.Action>) {
        assertTrue {
            actions.any {
                it is StepQuizCodeBlanksFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent
            }
        }
    }

    private fun stubContentState(
        step: Step = Step.stub(id = 1),
        codeBlocks: List<CodeBlock>,
        onboardingState: OnboardingState = OnboardingState.Unavailable
    ): StepQuizCodeBlanksFeature.State.Content =
        StepQuizCodeBlanksFeature.State.Content(
            step = step,
            codeBlocks = codeBlocks,
            onboardingState = onboardingState
        )
}