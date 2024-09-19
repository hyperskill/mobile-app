package org.hyperskill.step_quiz_code_blanks.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksOnboardingReducer
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.step.domain.model.stub

class StepQuizCodeBlanksReducerOnboardingTest {
    private val reducer = StepQuizCodeBlanksReducer.stub()

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
    fun `Onboarding should be available for first program`() {
        StepQuizCodeBlanksOnboardingReducer.Companion.FIRST_PROGRAM_STEP_IDS.forEach { stepId ->
            val initialState = StepQuizCodeBlanksFeature.State.Idle
            val (state, _) = reducer.reduce(
                initialState,
                StepQuizCodeBlanksFeature.InternalMessage.Initialize(Step.stub(id = stepId))
            )

            assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
            assertTrue(state.onboardingState is OnboardingState.FirstProgram.HighlightSuggestions)
        }
    }

    @Test
    fun `Onboarding should be available for delete button`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, _) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.InternalMessage.Initialize(
                Step.stub(id = StepQuizCodeBlanksOnboardingReducer.Companion.DELETE_BUTTON_STEP_ID)
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertTrue(state.onboardingState is OnboardingState.HighlightDeleteButton)
    }

    @Test
    fun `Onboarding should be available for enter button`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, _) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.InternalMessage.Initialize(
                Step.stub(id = StepQuizCodeBlanksOnboardingReducer.Companion.ENTER_BUTTON_STEP_ID)
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertTrue(state.onboardingState is OnboardingState.HighlightEnterButton)
    }

    @Test
    fun `Onboarding should be available for space button`() {
        val initialState = StepQuizCodeBlanksFeature.State.Idle
        val (state, _) = reducer.reduce(
            initialState,
            StepQuizCodeBlanksFeature.InternalMessage.Initialize(
                Step.stub(id = StepQuizCodeBlanksOnboardingReducer.Companion.SPACE_BUTTON_STEP_ID)
            )
        )

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertTrue(state.onboardingState is OnboardingState.HighlightSpaceButton)
    }

    @Test
    fun `Onboarding SuggestionClicked should update onboardingState to HighlightCallToActionButton`() {
        val suggestion = Suggestion.ConstantString("suggestion")
        val initialState = StepQuizCodeBlanksFeature.State.Content.stub(
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
            onboardingState = OnboardingState.FirstProgram.HighlightSuggestions
        )

        val message = StepQuizCodeBlanksFeature.Message.SuggestionClicked(suggestion)
        val (state, _) = reducer.reduce(initialState, message)

        assertTrue(state is StepQuizCodeBlanksFeature.State.Content)
        assertEquals(OnboardingState.FirstProgram.HighlightCallToActionButton, state.onboardingState)
    }
}