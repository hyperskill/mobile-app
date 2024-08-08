package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockChildHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
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
            is Message.CodeBlockChildClicked -> handleCodeBlockChildClicked(state, message)
            Message.DeleteButtonClicked -> handleDeleteButtonClicked(state)
            Message.EnterButtonClicked -> handleEnterButtonClicked(state)
        } ?: (state to emptySet())

    private fun initialize(
        message: InternalMessage.Initialize
    ): StepQuizCodeBlanksReducerResult =
        State.Content(
            step = message.step,
            codeBlocks = listOf(
                createBlankCodeBlock(
                    isActive = true,
                    isVariableSuggestionAvailable = StepQuizCodeBlanksFeature.isVariableSuggestionsAvailable(
                        step = message.step
                    )
                )
            )
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

        val newCodeBlock =
            when (val activeCodeBlock = state.codeBlocks[activeCodeBlockIndex]) {
                is CodeBlock.Blank -> when (message.suggestion) {
                    Suggestion.Print ->
                        CodeBlock.Print(
                            children = listOf(
                                CodeBlockChild.SelectSuggestion(
                                    isActive = true,
                                    suggestions = state.codeBlanksVariablesSuggestions +
                                        state.codeBlanksStringsSuggestions,
                                    selectedSuggestion = null
                                )
                            )
                        )
                    Suggestion.Variable ->
                        CodeBlock.Variable(
                            children = listOf(
                                CodeBlockChild.SelectSuggestion(
                                    isActive = true,
                                    suggestions = state.codeBlanksVariablesSuggestions,
                                    selectedSuggestion = null
                                ),
                                CodeBlockChild.SelectSuggestion(
                                    isActive = false,
                                    suggestions = state.codeBlanksStringsSuggestions,
                                    selectedSuggestion = null
                                )
                            )
                        )
                    else -> activeCodeBlock
                }
                is CodeBlock.Print -> {
                    activeCodeBlock.copy(
                        children = listOfNotNull(
                            activeCodeBlock.select?.copy(
                                selectedSuggestion = message.suggestion as? Suggestion.ConstantString
                            )
                        )
                    )
                }
                is CodeBlock.Variable -> {
                    activeCodeBlock.activeChildIndex()?.let { activeChildIndex ->
                        activeCodeBlock.copy(
                            children = activeCodeBlock.children.mutate {
                                set(
                                    activeChildIndex,
                                    activeCodeBlock.children[activeChildIndex].copy(
                                        selectedSuggestion = message.suggestion as? Suggestion.ConstantString
                                    )
                                )
                            }
                        )
                    } ?: activeCodeBlock
                }
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
                    set(it, setCodeBlockIsActive(codeBlock = state.codeBlocks[it], isActive = false))
                }
                set(targetCodeBlockIndex, setCodeBlockIsActive(codeBlock = targetCodeBlock, isActive = true))
            }
            state.copy(codeBlocks = newCodeBlocks) to actions
        }
    }

    private fun handleCodeBlockChildClicked(
        state: State,
        message: Message.CodeBlockChildClicked
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val targetCodeBlockIndex = message.codeBlockItem.id
        val targetCodeBlock = state.codeBlocks.getOrNull(index = targetCodeBlockIndex)

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedCodeBlockChildHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = targetCodeBlock,
                    codeBlockChild = targetCodeBlock?.children?.getOrNull(index = message.codeBlockChildItem.id)
                )
            )
        )

        return when (targetCodeBlock) {
            is CodeBlock.Variable -> {
                val newCodeBlocks = state.codeBlocks.mutate {
                    state.activeCodeBlockIndex()?.let {
                        set(it, setCodeBlockIsActive(codeBlock = state.codeBlocks[it], isActive = false))
                    }
                    set(
                        targetCodeBlockIndex,
                        targetCodeBlock.copy(
                            children = targetCodeBlock.children.mapIndexed { index, child ->
                                if (index == message.codeBlockChildItem.id) {
                                    child.copy(isActive = true)
                                } else {
                                    child.copy(isActive = false)
                                }
                            }
                        )
                    )
                }
                state.copy(codeBlocks = newCodeBlocks) to actions
            }
            else -> state to actions
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
                    set(nextActiveIndex, setCodeBlockIsActive(codeBlock = it, isActive = true))
                }
                removeAt(activeCodeBlockIndex)
            }
            val replaceActiveCodeWithBlank = {
                set(
                    activeCodeBlockIndex,
                    createBlankCodeBlock(
                        isActive = true,
                        isVariableSuggestionAvailable = state.isVariableSuggestionsAvailable
                    )
                )
            }

            when (val activeCodeBlock = state.codeBlocks[activeCodeBlockIndex]) {
                is CodeBlock.Blank -> {
                    if (state.codeBlocks.size > 1) {
                        removeActiveCodeBlockAndSetNextActive()
                    }
                }
                is CodeBlock.Print -> {
                    if (activeCodeBlock.select?.selectedSuggestion != null) {
                        set(
                            activeCodeBlockIndex,
                            activeCodeBlock.copy(
                                children = listOfNotNull(
                                    activeCodeBlock.select?.copy(selectedSuggestion = null)
                                )
                            )
                        )
                    } else if (state.codeBlocks.size > 1) {
                        removeActiveCodeBlockAndSetNextActive()
                    } else {
                        replaceActiveCodeWithBlank()
                    }
                }
                is CodeBlock.Variable -> {
                    val activeChildIndex = activeCodeBlock.activeChildIndex() ?: return@mutate
                    val activeChild = activeCodeBlock.children[activeChildIndex]

                    if (activeChild.selectedSuggestion != null) {
                        set(
                            activeCodeBlockIndex,
                            activeCodeBlock.copy(
                                children = activeCodeBlock.children.mutate {
                                    set(
                                        activeChildIndex,
                                        activeChild.copy(selectedSuggestion = null)
                                    )
                                }
                            )
                        )
                    } else if (
                        activeCodeBlock.name?.selectedSuggestion == null &&
                        activeCodeBlock.value?.selectedSuggestion == null
                    ) {
                        if (state.codeBlocks.size > 1) {
                            removeActiveCodeBlockAndSetNextActive()
                        } else {
                            replaceActiveCodeWithBlank()
                        }
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
                set(
                    activeCodeBlockIndex,
                    setCodeBlockIsActive(codeBlock = state.codeBlocks[activeCodeBlockIndex], isActive = false)
                )
                add(
                    activeCodeBlockIndex + 1,
                    createBlankCodeBlock(
                        isActive = true,
                        isVariableSuggestionAvailable = state.isVariableSuggestionsAvailable
                    )
                )
            }
            state.copy(codeBlocks = newCodeBlocks) to actions
        } else {
            state to actions
        }
    }

    private fun setCodeBlockIsActive(codeBlock: CodeBlock, isActive: Boolean): CodeBlock =
        when (codeBlock) {
            is CodeBlock.Blank -> codeBlock.copy(isActive = isActive)
            is CodeBlock.Print -> codeBlock.copy(children = listOfNotNull(codeBlock.select?.copy(isActive = isActive)))
            is CodeBlock.Variable -> {
                if (isActive) {
                    if (codeBlock.activeChild() != null) {
                        codeBlock
                    } else {
                        codeBlock.copy(
                            children = codeBlock.children.mapIndexed { index, child ->
                                if (index == 0) {
                                    child.copy(isActive = true)
                                } else {
                                    child.copy(isActive = false)
                                }
                            }
                        )
                    }
                } else {
                    codeBlock.copy(children = codeBlock.children.map { it.copy(isActive = false) })
                }
            }
        }

    private fun createBlankCodeBlock(
        isActive: Boolean,
        isVariableSuggestionAvailable: Boolean
    ): CodeBlock.Blank =
        CodeBlock.Blank(
            isActive = isActive,
            suggestions = if (isVariableSuggestionAvailable) {
                listOf(Suggestion.Print, Suggestion.Variable)
            } else {
                listOf(Suggestion.Print)
            }
        )
}