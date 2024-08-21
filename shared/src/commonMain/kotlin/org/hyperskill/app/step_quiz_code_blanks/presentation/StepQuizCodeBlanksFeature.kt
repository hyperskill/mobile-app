package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.codeBlanksStringsSuggestions
import org.hyperskill.app.step.domain.model.codeBlanksVariablesSuggestions
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalAction.ParentFeatureActionRequested
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState

object StepQuizCodeBlanksFeature {
    private const val ONBOARDING_STEP_ID = 47329L

    internal fun isCodeBlanksFeatureAvailable(step: Step): Boolean =
        step.block.options.codeBlanksEnabled == true

    internal fun isVariableSuggestionsAvailable(step: Step): Boolean =
        step.block.options.codeBlanksVariables?.isNotEmpty() == true

    internal fun isOnboardingAvailable(step: Step): Boolean =
        step.id == ONBOARDING_STEP_ID

    internal fun initialState(): State = State.Idle

    sealed interface State {
        data object Idle : State

        data class Content(
            val step: Step,
            val codeBlocks: List<CodeBlock>,
            val onboardingState: OnboardingState = OnboardingState.Unavailable
        ) : State {
            internal val codeBlanksStringsSuggestions: List<Suggestion.ConstantString> =
                step.codeBlanksStringsSuggestions()

            internal val codeBlanksVariablesSuggestions: List<Suggestion.ConstantString> =
                step.codeBlanksVariablesSuggestions()
        }
    }

    sealed interface OnboardingState {
        data object Unavailable : OnboardingState
        data object HighlightSuggestions : OnboardingState
        data object HighlightCallToActionButton : OnboardingState
    }

    sealed interface Message {
        data class SuggestionClicked(val suggestion: Suggestion) : Message

        data class CodeBlockClicked(val codeBlockItem: StepQuizCodeBlanksViewState.CodeBlockItem) : Message
        data class CodeBlockChildClicked(
            val codeBlockItem: StepQuizCodeBlanksViewState.CodeBlockItem,
            val codeBlockChildItem: StepQuizCodeBlanksViewState.CodeBlockChildItem
        ) : Message

        data object DeleteButtonClicked : Message
        data object EnterButtonClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val step: Step) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction

        data class ParentFeatureActionRequested(val parentFeatureAction: ParentFeatureAction) : InternalAction
    }

    internal sealed interface ParentFeatureAction {
        data object HighlightCallToActionButton : ParentFeatureAction
    }
}

internal fun Set<StepQuizCodeBlanksFeature.Action>.getRequestedParentFeatureAction(): ParentFeatureActionRequested? =
    filterIsInstance<ParentFeatureActionRequested>().firstOrNull()