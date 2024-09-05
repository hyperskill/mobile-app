package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.core.utils.indexOfFirstOrNull
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockChildHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedCodeBlockHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDecreaseIndentLevelHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedSpaceHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.analytic.StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Action
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalAction
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalMessage
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Message
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.State
import ru.nobird.app.core.model.cast
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
            Message.SpaceButtonClicked -> handleSpaceButtonClicked(state)
            Message.DecreaseIndentLevelButtonClicked -> handleDecreaseIndentLevelButtonClicked(state)
        } ?: (state to emptySet())

    private fun initialize(
        message: InternalMessage.Initialize
    ): StepQuizCodeBlanksReducerResult =
        State.Content(
            step = message.step,
            codeBlocks = createInitialCodeBlocks(step = message.step),
            onboardingState = if (StepQuizCodeBlanksFeature.isOnboardingAvailable(message.step)) {
                OnboardingState.HighlightSuggestions
            } else {
                OnboardingState.Unavailable
            }
        ) to emptySet()

    private fun handleSuggestionClicked(
        state: State,
        message: Message.SuggestionClicked
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val activeCodeBlockIndex = state.activeCodeBlockIndex()
        val activeCodeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] }

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = activeCodeBlock,
                    suggestion = message.suggestion
                )
            )
        )

        if (activeCodeBlock == null) {
            return state to actions
        }

        val newCodeBlock =
            when (activeCodeBlock) {
                is CodeBlock.Blank -> when (message.suggestion) {
                    Suggestion.Print ->
                        CodeBlock.Print(
                            indentLevel = activeCodeBlock.indentLevel,
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
                            indentLevel = activeCodeBlock.indentLevel,
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
                    Suggestion.IfStatement ->
                        CodeBlock.IfStatement(
                            indentLevel = activeCodeBlock.indentLevel,
                            children = listOf(
                                CodeBlockChild.SelectSuggestion(
                                    isActive = true,
                                    suggestions = state.codeBlanksVariablesSuggestions +
                                        state.codeBlanksStringsSuggestions,
                                    selectedSuggestion = null
                                )
                            )
                        )
                    else -> activeCodeBlock
                }
                is CodeBlock.Print,
                is CodeBlock.IfStatement -> {
                    activeCodeBlock.activeChildIndex()?.let { activeChildIndex ->
                        val activeChild = activeCodeBlock.children[activeChildIndex] as CodeBlockChild.SelectSuggestion
                        val newChildren = activeCodeBlock.children
                            .mutate {
                                set(
                                    activeChildIndex,
                                    activeChild.copy(
                                        selectedSuggestion = message.suggestion as? Suggestion.ConstantString
                                    )
                                )
                            }
                            .cast<List<CodeBlockChild.SelectSuggestion>>()

                        when (activeCodeBlock) {
                            is CodeBlock.Print -> activeCodeBlock.copy(children = newChildren)
                            is CodeBlock.IfStatement -> activeCodeBlock.copy(children = newChildren)
                            else -> activeCodeBlock
                        }
                    } ?: activeCodeBlock
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

                                val nextUnselectedChildIndex = this.indexOfFirstOrNull { it.selectedSuggestion == null }
                                if (nextUnselectedChildIndex != null) {
                                    set(
                                        nextUnselectedChildIndex,
                                        this[nextUnselectedChildIndex].copy(isActive = true)
                                    )
                                    set(
                                        activeChildIndex,
                                        this[activeChildIndex].copy(isActive = false)
                                    )
                                }
                            }
                        )
                    } ?: activeCodeBlock
                }
            }
        val newCodeBlocks = state.codeBlocks.mutate { set(activeCodeBlockIndex, newCodeBlock) }

        val isFulfilledOnboardingPrintCodeBlock =
            state.onboardingState is OnboardingState.HighlightSuggestions &&
                activeCodeBlock is CodeBlock.Print && activeCodeBlock.children.any { it.selectedSuggestion == null } &&
                newCodeBlock is CodeBlock.Print && newCodeBlock.children.all { it.selectedSuggestion != null }
        val (onboardingState, onboardingActions) =
            if (isFulfilledOnboardingPrintCodeBlock) {
                OnboardingState.HighlightCallToActionButton to
                    setOf(
                        InternalAction.ParentFeatureActionRequested(
                            StepQuizCodeBlanksFeature.ParentFeatureAction.HighlightCallToActionButton
                        )
                    )
            } else {
                state.onboardingState to emptySet()
            }

        return state.copy(
            codeBlocks = newCodeBlocks,
            onboardingState = onboardingState
        ) to actions + onboardingActions
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

        val newChildren = when (targetCodeBlock) {
            is CodeBlock.Print,
            is CodeBlock.Variable,
            is CodeBlock.IfStatement -> {
                targetCodeBlock.children.mapIndexed { index, child ->
                    require(child is CodeBlockChild.SelectSuggestion)
                    if (index == message.codeBlockChildItem.id) {
                        child.copy(isActive = true)
                    } else {
                        child.copy(isActive = false)
                    }
                }
            }
            else -> null
        }

        val newCodeBlocks = state.codeBlocks.mutate {
            newChildren?.let { newChildren ->
                state.activeCodeBlockIndex()?.let {
                    set(it, setCodeBlockIsActive(codeBlock = state.codeBlocks[it], isActive = false))
                }

                targetCodeBlock?.let { targetCodeBlock ->
                    set(
                        targetCodeBlockIndex,
                        when (targetCodeBlock) {
                            is CodeBlock.Print -> targetCodeBlock.copy(children = newChildren)
                            is CodeBlock.Variable -> targetCodeBlock.copy(children = newChildren)
                            is CodeBlock.IfStatement -> targetCodeBlock.copy(children = newChildren)
                            else -> targetCodeBlock
                        }
                    )
                }
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

        val activeCodeBlockIndex = state.activeCodeBlockIndex()
        val activeCodeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] }

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedDeleteHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = activeCodeBlock
                )
            )
        )

        if (activeCodeBlock == null) {
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
                        indentLevel = activeCodeBlock.indentLevel,
                        isVariableSuggestionAvailable = state.isVariableSuggestionsAvailable
                    )
                )
            }

            when (activeCodeBlock) {
                is CodeBlock.Blank -> {
                    if (state.codeBlocks.size > 1) {
                        removeActiveCodeBlockAndSetNextActive()
                    }
                }
                is CodeBlock.Print -> {
                    val activeChildIndex = activeCodeBlock.activeChildIndex() ?: return@mutate
                    val activeChild = activeCodeBlock.children[activeChildIndex]

                    when {
                        activeChild.selectedSuggestion != null ->
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

                        activeChildIndex > 0 ->
                            set(
                                activeCodeBlockIndex,
                                activeCodeBlock.copy(
                                    children = activeCodeBlock.children.mutate {
                                        set(
                                            activeChildIndex - 1,
                                            this[activeChildIndex - 1].copy(isActive = true)
                                        )
                                        removeAt(activeChildIndex)
                                    }
                                )
                            )

                        state.codeBlocks.size > 1 ->
                            removeActiveCodeBlockAndSetNextActive()

                        else ->
                            replaceActiveCodeWithBlank()
                    }
                }
                is CodeBlock.Variable -> {
                    val activeChildIndex = activeCodeBlock.activeChildIndex() ?: return@mutate
                    val activeChild = activeCodeBlock.children[activeChildIndex]

                    when {
                        activeChild.selectedSuggestion != null ->
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

                        activeChildIndex > 1 ->
                            set(
                                activeCodeBlockIndex,
                                activeCodeBlock.copy(
                                    children = activeCodeBlock.children.mutate {
                                        set(
                                            activeChildIndex - 1,
                                            this[activeChildIndex - 1].copy(isActive = true)
                                        )
                                        removeAt(activeChildIndex)
                                    }
                                )
                            )

                        activeChildIndex == 0 || activeCodeBlock.areAllChildrenUnselected() ->
                            if (state.codeBlocks.size > 1) {
                                removeActiveCodeBlockAndSetNextActive()
                            } else {
                                replaceActiveCodeWithBlank()
                            }
                    }
                }
                is CodeBlock.IfStatement -> {
                    val activeChildIndex = activeCodeBlock.activeChildIndex() ?: return@mutate
                    val activeChild = activeCodeBlock.children[activeChildIndex]

                    val nextCodeBlock = state.codeBlocks.getOrNull(activeCodeBlockIndex + 1)

                    when {
                        activeChild.selectedSuggestion != null ->
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

                        activeChildIndex > 0 ->
                            set(
                                activeCodeBlockIndex,
                                activeCodeBlock.copy(
                                    children = activeCodeBlock.children.mutate {
                                        set(
                                            activeChildIndex - 1,
                                            this[activeChildIndex - 1].copy(isActive = true)
                                        )
                                        removeAt(activeChildIndex)
                                    }
                                )
                            )

                        (activeChildIndex == 0 || activeCodeBlock.areAllChildrenUnselected()) &&
                            (nextCodeBlock?.let { it.indentLevel == activeCodeBlock.indentLevel } ?: true) ->
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
        val activeCodeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] }

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedEnterHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = activeCodeBlock
                )
            )
        )

        return if (activeCodeBlock != null) {
            val indentLevel =
                when (activeCodeBlock) {
                    is CodeBlock.IfStatement -> activeCodeBlock.indentLevel + 1
                    else -> activeCodeBlock.indentLevel
                }

            val newCodeBlocks = state.codeBlocks.mutate {
                set(
                    activeCodeBlockIndex,
                    setCodeBlockIsActive(codeBlock = state.codeBlocks[activeCodeBlockIndex], isActive = false)
                )
                add(
                    activeCodeBlockIndex + 1,
                    createBlankCodeBlock(
                        isActive = true,
                        indentLevel = indentLevel,
                        isVariableSuggestionAvailable = state.isVariableSuggestionsAvailable
                    )
                )
            }
            state.copy(codeBlocks = newCodeBlocks) to actions
        } else {
            state to actions
        }
    }

    private fun handleSpaceButtonClicked(
        state: State
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val activeCodeBlockIndex = state.activeCodeBlockIndex()
        val activeCodeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] }

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedSpaceHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = activeCodeBlock
                )
            )
        )

        if (activeCodeBlock == null) {
            return state to actions
        }

        val newChildren = when (activeCodeBlock) {
            is CodeBlock.Print,
            is CodeBlock.Variable,
            is CodeBlock.IfStatement -> {
                activeCodeBlock.activeChildIndex()?.let { activeChildIndex ->
                    val activeChild = activeCodeBlock.children[activeChildIndex] as CodeBlockChild.SelectSuggestion

                    val newChildSuggestions = when {
                        activeChild.selectedSuggestion?.isOpeningParentheses == true ->
                            state.codeBlanksVariablesSuggestions + state.codeBlanksStringsSuggestions

                        activeChild.selectedSuggestion?.isClosingParentheses == true ->
                            state.codeBlanksOperationsSuggestions

                        activeChild.selectedSuggestion in state.codeBlanksStringsSuggestions ||
                            activeChild.selectedSuggestion in state.codeBlanksVariablesSuggestions ->
                            state.codeBlanksOperationsSuggestions

                        activeChild.selectedSuggestion in state.codeBlanksOperationsSuggestions ->
                            state.codeBlanksVariablesSuggestions + state.codeBlanksStringsSuggestions

                        else -> emptyList()
                    }

                    val newChild = CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = newChildSuggestions,
                        selectedSuggestion = null
                    )

                    activeCodeBlock.children
                        .mutate {
                            set(activeChildIndex, activeChild.copy(isActive = false))
                            add(activeChildIndex + 1, newChild)
                        }
                        .cast<List<CodeBlockChild.SelectSuggestion>>()
                }
            }
            else -> null
        }

        val newCodeBlocks = state.codeBlocks.mutate {
            newChildren?.let {
                set(
                    activeCodeBlockIndex,
                    when (activeCodeBlock) {
                        is CodeBlock.Print -> activeCodeBlock.copy(children = newChildren)
                        is CodeBlock.Variable -> activeCodeBlock.copy(children = newChildren)
                        is CodeBlock.IfStatement -> activeCodeBlock.copy(children = newChildren)
                        else -> activeCodeBlock
                    }
                )
            }
        }

        return state.copy(codeBlocks = newCodeBlocks) to actions
    }

    private fun handleDecreaseIndentLevelButtonClicked(
        state: State
    ): StepQuizCodeBlanksReducerResult? {
        if (state !is State.Content) {
            return null
        }

        val activeCodeBlockIndex = state.activeCodeBlockIndex()
        val activeCodeBlock = activeCodeBlockIndex?.let { state.codeBlocks[it] }

        val actions = setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizCodeBlanksClickedDecreaseIndentLevelHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    codeBlock = activeCodeBlock
                )
            )
        )

        if (activeCodeBlock == null || activeCodeBlock.indentLevel < 1) {
            return state to actions
        }
        val newIndentLevel = activeCodeBlock.indentLevel - 1

        return state.copy(
            codeBlocks = state.codeBlocks.mutate {
                set(
                    activeCodeBlockIndex,
                    when (activeCodeBlock) {
                        is CodeBlock.Blank -> activeCodeBlock.copy(indentLevel = newIndentLevel)
                        is CodeBlock.Print -> activeCodeBlock.copy(indentLevel = newIndentLevel)
                        is CodeBlock.Variable -> activeCodeBlock.copy(indentLevel = newIndentLevel)
                        is CodeBlock.IfStatement -> activeCodeBlock.copy(indentLevel = newIndentLevel)
                    }
                )
            }
        ) to actions
    }

    private fun setCodeBlockIsActive(codeBlock: CodeBlock, isActive: Boolean): CodeBlock =
        when (codeBlock) {
            is CodeBlock.Blank -> codeBlock.copy(isActive = isActive)
            is CodeBlock.Print,
            is CodeBlock.Variable,
            is CodeBlock.IfStatement -> {
                if (isActive) {
                    if (codeBlock.activeChild() != null) {
                        codeBlock
                    } else {
                        val newChildren = codeBlock.children.mapIndexed { index, child ->
                            require(child is CodeBlockChild.SelectSuggestion)
                            if (index == 0) {
                                child.copy(isActive = true)
                            } else {
                                child.copy(isActive = false)
                            }
                        }
                        when (codeBlock) {
                            is CodeBlock.Print -> codeBlock.copy(children = newChildren)
                            is CodeBlock.Variable -> codeBlock.copy(children = newChildren)
                            is CodeBlock.IfStatement -> codeBlock.copy(children = newChildren)
                            else -> codeBlock
                        }
                    }
                } else {
                    val newChildren = codeBlock.children.map { child ->
                        require(child is CodeBlockChild.SelectSuggestion)
                        child.copy(isActive = false)
                    }
                    when (codeBlock) {
                        is CodeBlock.Print -> codeBlock.copy(children = newChildren)
                        is CodeBlock.Variable -> codeBlock.copy(children = newChildren)
                        is CodeBlock.IfStatement -> codeBlock.copy(children = newChildren)
                        else -> codeBlock
                    }
                }
            }
        }

    private fun createBlankCodeBlock(
        isActive: Boolean,
        indentLevel: Int,
        isVariableSuggestionAvailable: Boolean
    ): CodeBlock.Blank =
        CodeBlock.Blank(
            isActive = isActive,
            indentLevel = indentLevel,
            suggestions = if (isVariableSuggestionAvailable) {
                listOf(Suggestion.Print, Suggestion.Variable, Suggestion.IfStatement)
            } else {
                listOf(Suggestion.Print)
            }
        )

    private fun createInitialCodeBlocks(step: Step): List<CodeBlock> =
        if (step.id == 47580L) {
            listOf(
                CodeBlock.Variable(
                    indentLevel = 0,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = step.codeBlanksVariablesSuggestions(),
                            selectedSuggestion = Suggestion.ConstantString("x")
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = step.codeBlanksStringsSuggestions(),
                            selectedSuggestion = Suggestion.ConstantString("1000")
                        )
                    )
                ),
                CodeBlock.Variable(
                    indentLevel = 0,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = step.codeBlanksVariablesSuggestions(),
                            selectedSuggestion = Suggestion.ConstantString("r")
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = step.codeBlanksStringsSuggestions(),
                            selectedSuggestion = Suggestion.ConstantString("5")
                        )
                    )
                ),
                CodeBlock.Variable(
                    indentLevel = 0,
                    children = listOf(
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = step.codeBlanksVariablesSuggestions(),
                            selectedSuggestion = Suggestion.ConstantString("y")
                        ),
                        CodeBlockChild.SelectSuggestion(
                            isActive = false,
                            suggestions = step.codeBlanksStringsSuggestions(),
                            selectedSuggestion = Suggestion.ConstantString("10")
                        )
                    )
                ),
                createBlankCodeBlock(
                    isActive = true,
                    indentLevel = 0,
                    isVariableSuggestionAvailable = StepQuizCodeBlanksFeature.isVariableSuggestionsAvailable(step)
                )
            )
        } else {
            listOf(
                createBlankCodeBlock(
                    isActive = true,
                    indentLevel = 0,
                    isVariableSuggestionAvailable = StepQuizCodeBlanksFeature.isVariableSuggestionsAvailable(step)
                )
            )
        }
}