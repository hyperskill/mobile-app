package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Action
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalAction
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalMessage
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.State

private typealias StepQuizCodeBlanksOnboardingReducerResult = Pair<OnboardingState, Set<Action>>

class StepQuizCodeBlanksOnboardingReducer {
    companion object {
        internal val FIRST_PROGRAM_STEP_IDS = setOf(47329L, 50984L)
        internal const val DELETE_BUTTON_STEP_ID = 50986L
        internal const val ENTER_BUTTON_STEP_ID = 50985L
        internal const val SPACE_BUTTON_STEP_ID = 50997L
    }

    internal fun reduceInitializeMessage(
        message: InternalMessage.Initialize
    ): OnboardingState =
        when (message.step.id) {
            in FIRST_PROGRAM_STEP_IDS -> OnboardingState.FirstProgram.HighlightSuggestions
            DELETE_BUTTON_STEP_ID -> OnboardingState.HighlightDeleteButton
            ENTER_BUTTON_STEP_ID -> OnboardingState.HighlightEnterButton
            SPACE_BUTTON_STEP_ID -> OnboardingState.HighlightSpaceButton
            else -> OnboardingState.Unavailable
        }

    internal fun reduceSuggestionClickedMessage(
        state: State.Content,
        activeCodeBlock: CodeBlock?,
        newCodeBlock: CodeBlock
    ): StepQuizCodeBlanksOnboardingReducerResult {
        val isFulfilledFirstProgramOnboarding =
            state.onboardingState is OnboardingState.FirstProgram.HighlightSuggestions &&
                activeCodeBlock is CodeBlock.Print && activeCodeBlock.hasAnyUnselectedChild() &&
                newCodeBlock is CodeBlock.Print && newCodeBlock.areAllChildrenSelected()
        return if (isFulfilledFirstProgramOnboarding) {
            OnboardingState.FirstProgram.HighlightCallToActionButton to
                setOf(
                    InternalAction.ParentFeatureActionRequested(
                        StepQuizCodeBlanksFeature.ParentFeatureAction.HighlightCallToActionButton
                    )
                )
        } else {
            state.onboardingState to emptySet()
        }
    }

    internal fun reduceDeleteButtonClickedMessage(
        state: State.Content
    ): StepQuizCodeBlanksOnboardingReducerResult =
        if (state.onboardingState is OnboardingState.HighlightDeleteButton) {
            OnboardingState.Unavailable to emptySet()
        } else {
            state.onboardingState to emptySet()
        }

    internal fun reduceEnterButtonClickedMessage(
        state: State.Content
    ): StepQuizCodeBlanksOnboardingReducerResult =
        if (state.onboardingState is OnboardingState.HighlightEnterButton) {
            OnboardingState.Unavailable to emptySet()
        } else {
            state.onboardingState to emptySet()
        }

    internal fun reduceSpaceButtonClickedMessage(
        state: State.Content
    ): StepQuizCodeBlanksOnboardingReducerResult =
        if (state.onboardingState is OnboardingState.HighlightSpaceButton) {
            OnboardingState.Unavailable to emptySet()
        } else {
            state.onboardingState to emptySet()
        }
}