package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Action
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalAction
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalMessage
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.State

class StepQuizCodeBlanksOnboardingReducer {
    companion object {
        private const val DELETE_BUTTON_STEP_ID = 50969L
        private const val SPACE_BUTTON_STEP_ID = 50970L
        private val PRINT_SUGGESTION_AND_CALL_TO_ACTION_STEP_IDS = setOf(47329L, 50968L)
    }

    internal fun reduceInitializeMessage(
        message: InternalMessage.Initialize
    ): OnboardingState =
        when (message.step.id) {
            in PRINT_SUGGESTION_AND_CALL_TO_ACTION_STEP_IDS ->
                OnboardingState.PrintSuggestionAndCallToAction.HighlightSuggestions
            DELETE_BUTTON_STEP_ID -> OnboardingState.HighlightDeleteButton
            SPACE_BUTTON_STEP_ID -> OnboardingState.HighlightSpaceButton
            else -> OnboardingState.Unavailable
        }

    internal fun reduceSuggestionClickedMessage(
        state: State.Content,
        activeCodeBlock: CodeBlock?,
        newCodeBlock: CodeBlock
    ): Pair<OnboardingState, Set<Action>> {
        val isFulfilledOnboardingPrintCodeBlock =
            state.onboardingState is OnboardingState.PrintSuggestionAndCallToAction.HighlightSuggestions &&
                activeCodeBlock is CodeBlock.Print && activeCodeBlock.hasAnyUnselectedChild() &&
                newCodeBlock is CodeBlock.Print && newCodeBlock.areAllChildrenSelected()
        return if (isFulfilledOnboardingPrintCodeBlock) {
            OnboardingState.PrintSuggestionAndCallToAction.HighlightCallToActionButton to
                setOf(
                    InternalAction.ParentFeatureActionRequested(
                        StepQuizCodeBlanksFeature.ParentFeatureAction.HighlightCallToActionButton
                    )
                )
        } else {
            state.onboardingState to emptySet()
        }
    }
}