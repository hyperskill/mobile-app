package org.hyperskill.step_quiz_code_blanks.view

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState

class StepQuizCodeBlanksViewStateTest {
    @Test
    fun `isActionButtonsHidden should be true when onboarding is available`() {
        val viewState = stubContentViewState(onboardingState = OnboardingState.HighlightSuggestions)
        assertTrue(viewState.isActionButtonsHidden)
    }

    @Test
    fun `isActionButtonsHidden should be false when onboarding is unavailable`() {
        val viewState = stubContentViewState(onboardingState = OnboardingState.Unavailable)
        assertFalse(viewState.isActionButtonsHidden)
    }

    @Test
    fun `isSuggestionsHighlightEffectActive should be true when onboardingState is HighlightSuggestions`() {
        val viewState = stubContentViewState(onboardingState = OnboardingState.HighlightSuggestions)
        assertTrue(viewState.isSuggestionsHighlightEffectActive)
    }

    private fun stubContentViewState(
        onboardingState: OnboardingState
    ): StepQuizCodeBlanksViewState.Content =
        StepQuizCodeBlanksViewState.Content(
            codeBlocks = emptyList(),
            suggestions = emptyList(),
            isDeleteButtonEnabled = false,
            isSpaceButtonHidden = false,
            isDecreaseIndentLevelButtonHidden = false,
            onboardingState = onboardingState
        )
}