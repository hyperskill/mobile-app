package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent
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
            Message.EnterButtonClicked -> handleEnterButtonClicked(state)
        } ?: (state to emptySet())

    private fun initialize(
        message: InternalMessage.Initialize
    ): StepQuizCodeBlanksReducerResult =
        State.Content(
            step = message.step,
            codeBlocks = listOf(CodeBlock.Blank(isActive = true))
        ) to emptySet()

    private fun handleSuggestionClicked(
        state: State,
        message: Message.SuggestionClicked
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val activeCodeBlockIndex = state.activeCodeBlockIndex()

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
        val newCodeBlocks = state.codeBlocks.mutate { set(activeCodeBlockIndex, newCodeBlock) }

        return state.copy(codeBlocks = newCodeBlocks) to actions
    }

    private fun handleCodeBlockClicked(
        state: State,
        message: Message.CodeBlockClicked
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val targetCodeBlockIndex = message.codeBlockItem.id
        val targetCodeBlock = state.codeBlocks.getOrNull(index = targetCodeBlockIndex)

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = targetCodeBlock
                )
            )
        )

        return if (targetCodeBlock == null || targetCodeBlock.isActive) {
            state to actions
        } else {
            val newCodeBlocks = state.codeBlocks.mutate {
                state.activeCodeBlockIndex()?.let {
                    set(it, copyCodeBlock(state.codeBlocks[it], isActive = false))
                }
                set(targetCodeBlockIndex, copyCodeBlock(targetCodeBlock, isActive = true))
            }
            state.copy(codeBlocks = newCodeBlocks) to actions
        }
    }

    private fun handleDeleteButtonClicked(
        state: State
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val activeCodeBlockIndex = state.activeCodeBlockIndex()

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

        val newCodeBlocks = state.codeBlocks.mutate {
            val removeActiveCodeBlockAndSetNextActive = {
                val nextActiveIndex =
                    if (activeCodeBlockIndex > 0) {
                        activeCodeBlockIndex - 1
                    } else {
                        activeCodeBlockIndex + 1
                    }
                state.codeBlocks.getOrNull(nextActiveIndex)?.let {
                    set(nextActiveIndex, copyCodeBlock(it, isActive = true))
                }
                removeAt(activeCodeBlockIndex)
            }

            when (val activeCodeBlock = state.codeBlocks[activeCodeBlockIndex]) {
                is CodeBlock.Blank -> {
                    if (state.codeBlocks.size > 1) {
                        removeActiveCodeBlockAndSetNextActive()
                    }
                }
                is CodeBlock.Print -> {
                    if (activeCodeBlock.selectedSuggestion != null) {
                        set(activeCodeBlockIndex, activeCodeBlock.copy(selectedSuggestion = null))
                    } else if (state.codeBlocks.size > 1) {
                        removeActiveCodeBlockAndSetNextActive()
                    } else {
                        set(activeCodeBlockIndex, CodeBlock.Blank(isActive = true))
                    }
                }
            }
        }

        return state.copy(codeBlocks = newCodeBlocks) to actions
    }

    private fun handleEnterButtonClicked(
        state: State
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val activeCodeBlockIndex = state.activeCodeBlockIndex()

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] }
                )
            )
        )

        return if (activeCodeBlockIndex != null) {
            val newCodeBlocks = state.codeBlocks.mutate {
                set(activeCodeBlockIndex, copyCodeBlock(state.codeBlocks[activeCodeBlockIndex], isActive = false))
                add(activeCodeBlockIndex + 1, CodeBlock.Blank(isActive = true))
            }
            state.copy(codeBlocks = newCodeBlocks) to actions
        } else {
            state to actions
        }
    }

    private fun copyCodeBlock(codeBlock: CodeBlock, isActive: Boolean): CodeBlock =
        when (codeBlock) {
            is CodeBlock.Blank -> codeBlock.copy(isActive = isActive)
            is CodeBlock.Print -> codeBlock.copy(isActive = isActive)
        }
}