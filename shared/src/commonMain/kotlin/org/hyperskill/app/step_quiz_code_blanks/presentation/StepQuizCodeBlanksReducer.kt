package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Action
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalAction
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalMessage
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Message
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.State
import ru.nobird.app.core.model.mutate
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias StepQuizCodeBlanksReducerResult = Pair<State, Set<Action>>

class StepQuizCodeBlanksReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): StepQuizCodeBlanksReducerResult =
        when (message) {
            is InternalMessage.Initialize -> initialize(message)
            is Message.SuggestionClicked -> handleSuggestionClicked(state, message)
            is Message.CodeBlockClicked -> handleCodeBlockClicked(state, message)
            Message.DeleteButtonClicked -> handleDeleteButtonClicked(state)
        } ?: (state to emptySet())

    private fun initialize(
        message: InternalMessage.Initialize
    ): StepQuizCodeBlanksReducerResult =
        State.Content(
            step = message.step,
            attempt = message.attempt,
            codeBlocks = listOf(CodeBlock.Blank(isActive = true))
        ) to emptySet()

    private fun handleSuggestionClicked(
        state: State,
        message: Message.SuggestionClicked
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val activeCodeBlockIndex = state.activeCodeBlockIndex

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] },
                    suggestion = message.suggestion
                )
            )
        )

        if (activeCodeBlockIndex == null) {
            return state to actions
        }
        val activeCodeBlock = state.codeBlocks[activeCodeBlockIndex]

        if (!activeCodeBlock.suggestions.contains(message.suggestion)) {
            return state to actions
        }

        val newCodeBlock =
            when (activeCodeBlock) {
                is CodeBlock.Blank ->
                    CodeBlock.Print(
                        isActive = true,
                        suggestions = state.codeBlanksStringsSuggestions,
                        selectedSuggestion = null
                    )
                is CodeBlock.Print ->
                    activeCodeBlock.copy(
                        selectedSuggestion = message.suggestion as? Suggestion.ConstantString
                    )
            }

        val newCodeBlocks =
            if (activeCodeBlockIndex == state.codeBlocks.size - 1 && newCodeBlock.selectedSuggestion != null) {
                state.codeBlocks.mutate {
                    set(activeCodeBlockIndex, newCodeBlock.copy(isActive = false))
                    add(CodeBlock.Blank(isActive = true))
                }
            } else {
                state.codeBlocks.mutate { set(activeCodeBlockIndex, newCodeBlock) }
            }

        return state.copy(codeBlocks = newCodeBlocks) to actions
    }

    private fun handleCodeBlockClicked(
        state: State,
        message: Message.CodeBlockClicked
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val targetCodeBlock = state.codeBlocks.getOrNull(index = message.codeBlockItem.id)
        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = targetCodeBlock
                )
            )
        )

        if (targetCodeBlock?.isActive == true) {
            return state to actions
        }

        val newCodeBlocks = state.codeBlocks.mapIndexed { index, codeBlock ->
            val isActive = index == message.codeBlockItem.id
            when (codeBlock) {
                is CodeBlock.Blank -> codeBlock.copy(isActive = isActive)
                is CodeBlock.Print -> codeBlock.copy(isActive = isActive)
            }
        }

        return state.copy(codeBlocks = newCodeBlocks) to actions
    }

    private fun handleDeleteButtonClicked(
        state: State
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val activeCodeBlockIndex = state.activeCodeBlockIndex

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] }
                )
            )
        )

        if (activeCodeBlockIndex == null) {
            return state to actions
        }

        when (val activeCodeBlock = state.codeBlocks[activeCodeBlockIndex]) {
            is CodeBlock.Blank -> return state to actions
            is CodeBlock.Print -> {
                val newCodeBlocks = state.codeBlocks.mutate {
                    if (activeCodeBlock.selectedSuggestion != null) {
                        set(activeCodeBlockIndex, activeCodeBlock.copy(selectedSuggestion = null))
                    } else if (state.codeBlocks.size > 1) {
                        val nextActiveIndex =
                            if (activeCodeBlockIndex < state.codeBlocks.size - 1) {
                                activeCodeBlockIndex + 1
                            } else {
                                activeCodeBlockIndex - 1
                            }

                        val newNextActiveCodeBlock =
                            when (val nextCodeBlock = state.codeBlocks.getOrNull(nextActiveIndex)) {
                                is CodeBlock.Blank -> nextCodeBlock.copy(isActive = true)
                                is CodeBlock.Print -> nextCodeBlock.copy(isActive = true)
                                null -> null
                            }
                        newNextActiveCodeBlock?.let { set(nextActiveIndex, it) }

                        removeAt(activeCodeBlockIndex)
                    }
                }
                return state.copy(codeBlocks = newCodeBlocks) to actions
            }
        }
    }
}