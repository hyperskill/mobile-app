package org.hyperskill.step_quiz_code_blanks.view

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState

class StepQuizCodeBlanksViewStateTest {
    @Test
    fun `isActionButtonsHidden should be true when onboarding FirstProgram`() {
        listOf(
            OnboardingState.FirstProgram.HighlightSuggestions,
            OnboardingState.FirstProgram.HighlightCallToActionButton
        ).forEach { onboardingState ->
            val viewState = stubContentViewState(onboardingState = onboardingState)
            assertTrue(viewState.isActionButtonsHidden)
        }
    }

    @Test
    fun `isActionButtonsHidden should be false when onboarding is not PrintSuggestionAndCallToAction`() {
        listOf(
            OnboardingState.HighlightDeleteButton,
            OnboardingState.HighlightSpaceButton,
            OnboardingState.Unavailable
        ).forEach { onboardingState ->
            val viewState = stubContentViewState(onboardingState = onboardingState)
            assertFalse(viewState.isActionButtonsHidden)
        }
    }

    /* ktlint-disable */
    @Test
    fun `isDeleteButtonHighlightEffectActive should be true when onboardingState is HighlightDeleteButton and isDeleteButtonEnabled`() {
        val viewState = stubContentViewState(
            isDeleteButtonEnabled = true,
            onboardingState = OnboardingState.HighlightDeleteButton
        )
        assertTrue(viewState.isDeleteButtonHighlightEffectActive)
    }

    /* ktlint-disable */
    @Test
    fun `isDeleteButtonHighlightEffectActive should be false when onboardingState is HighlightDeleteButton and isDeleteButtonEnabled is false`() {
        val viewState = stubContentViewState(
            isDeleteButtonEnabled = false,
            onboardingState = OnboardingState.HighlightDeleteButton
        )
        assertFalse(viewState.isDeleteButtonHighlightEffectActive)
    }

    @Test
    fun `isEnterButtonHighlightEffectActive should be true when onboardingState is HighlightEnterButton`() {
        val viewState = stubContentViewState(
            onboardingState = OnboardingState.HighlightEnterButton
        )
        assertTrue(viewState.isEnterButtonHighlightEffectActive)
    }

    /* ktlint-disable */
    @Test
    fun `isSpaceButtonHighlightEffectActive should be true when onboardingState is HighlightSpaceButton and isSpaceButtonHidden is false`() {
        val viewState = stubContentViewState(
            onboardingState = OnboardingState.HighlightSpaceButton
        )
        assertTrue(viewState.isSpaceButtonHighlightEffectActive)
    }

    /* ktlint-disable */
    @Test
    fun `isSpaceButtonHighlightEffectActive should be false when onboardingState is HighlightSpaceButton and isSpaceButtonHidden is true`() {
        val viewState = stubContentViewState(
            isSpaceButtonHidden = true,
            onboardingState = OnboardingState.HighlightSpaceButton
        )
        assertFalse(viewState.isSpaceButtonHighlightEffectActive)
    }

    /* ktlint-disable */
    @Test
    fun `isSuggestionsHighlightEffectActive should be true when onboardingState is FirstProgram_HighlightSuggestions`() {
        val viewState = stubContentViewState(
            onboardingState = OnboardingState.FirstProgram.HighlightSuggestions
        )
        assertTrue(viewState.isSuggestionsHighlightEffectActive)
    }

    private fun stubContentViewState(
        isDeleteButtonEnabled: Boolean = false,
        isSpaceButtonHidden: Boolean = false,
        isDecreaseIndentLevelButtonHidden: Boolean = false,
        onboardingState: OnboardingState
    ): StepQuizCodeBlanksViewState.Content =
        StepQuizCodeBlanksViewState.Content(
            codeBlocks = emptyList(),
            suggestions = emptyList(),
            isDeleteButtonEnabled = isDeleteButtonEnabled,
            isSpaceButtonHidden = isSpaceButtonHidden,
            isDecreaseIndentLevelButtonHidden = isDecreaseIndentLevelButtonHidden,
            onboardingState = onboardingState
        )
}