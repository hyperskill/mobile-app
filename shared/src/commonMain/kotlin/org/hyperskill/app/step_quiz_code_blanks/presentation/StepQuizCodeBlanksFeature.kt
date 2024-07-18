package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.view.model.StepQuizCodeBlanksViewState

object StepQuizCodeBlanksFeature {
    internal fun isCodeBlanksFeatureAvailable(step: Step): Boolean =
        step.block.options.codeBlanksStrings.isNullOrEmpty().not()

    internal fun initialState(): State = State.Idle

    sealed interface State {
        data object Idle : State

        data class Content(
            val step: Step,
            val attempt: Attempt,
            val codeBlocks: List<CodeBlock>
        ) : State {
            internal val codeBlanksStringsSuggestions: List<Suggestion.ConstantString> =
                step.block.options.codeBlanksStrings?.map { Suggestion.ConstantString(it) } ?: emptyList()
        }
    }

    sealed interface Message {
        data class SuggestionClicked(val suggestion: Suggestion) : Message

        data class CodeBlockClicked(val codeBlockItem: StepQuizCodeBlanksViewState.CodeBlockItem) : Message

        data object DeleteButtonClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val step: Step, val attempt: Attempt) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}