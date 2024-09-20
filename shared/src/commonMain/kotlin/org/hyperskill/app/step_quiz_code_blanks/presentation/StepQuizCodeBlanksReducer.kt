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
import org.hyperskill.app.step_quiz_code_blanks.domain.model.template.CodeBlanksTemplateMapper
import org.hyperskill.app.step_quiz_code_blanks.domain.model.updatedChildren
import org.hyperskill.app.step_quiz_code_blanks.domain.model.updatedIndentLevel
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Action
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalAction
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalMessage
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Message
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.State
import ru.nobird.app.core.model.mutate
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias StepQuizCodeBlanksReducerResult = Pair<State, Set<Action>>

class StepQuizCodeBlanksReducer(
    private val stepRoute: StepRoute,
    private val stepQuizCodeBlanksOnboardingReducer: StepQuizCodeBlanksOnboardingReducer
) : StateReducer<State, Message, Action> {
    companion object;

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
            onboardingState = stepQuizCodeBlanksOnboardingReducer.reduceInitializeMessage(message)
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
                                    suggestions = state.variablesAndStringsSuggestions,
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
                                    suggestions = state.variablesSuggestions,
                                    selectedSuggestion = null
                                ),
                                CodeBlockChild.SelectSuggestion(
                                    isActive = false,
                                    suggestions = state.stringsSuggestions,
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
                                    suggestions = state.variablesAndStringsSuggestions,
                                    selectedSuggestion = null
                                )
                            )
                        )
                    Suggestion.ElifStatement ->
                        CodeBlock.ElifStatement(
                            indentLevel = activeCodeBlock.indentLevel,
                            children = listOf(
                                CodeBlockChild.SelectSuggestion(
                                    isActive = true,
                                    suggestions = state.variablesAndStringsSuggestions,
                                    selectedSuggestion = null
                                )
                            )
                        )
                    Suggestion.ElseStatement ->
                        CodeBlock.ElseStatement(
                            isActive = false,
                            indentLevel = activeCodeBlock.indentLevel
                        )
                    is Suggestion.ConstantString -> activeCodeBlock
                }

                is CodeBlock.Print,
                is CodeBlock.IfStatement,
                is CodeBlock.ElifStatement -> {
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
                        activeCodeBlock.updatedChildren(newChildren)
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

                is CodeBlock.ElseStatement -> activeCodeBlock
            }
        val newCodeBlocks = state.codeBlocks.mutate {
            set(activeCodeBlockIndex, newCodeBlock)

            if (newCodeBlock is CodeBlock.ElseStatement && activeCodeBlock !== newCodeBlock) {
                val blankInsertIndex = activeCodeBlockIndex + 1
                val blankIndentLevel = newCodeBlock.indentLevel + 1
                add(
                    blankInsertIndex,
                    CodeBlock.Blank(
                        isActive = true,
                        indentLevel = blankIndentLevel,
                        suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                            index = blankInsertIndex,
                            indentLevel = blankIndentLevel,
                            codeBlocks = this,
                            isVariableSuggestionAvailable = state.isVariableSuggestionsAvailable,
                            availableConditions = state.availableConditions
                        )
                    )
                )
            }
        }

        val (onboardingState, onboardingActions) =
            stepQuizCodeBlanksOnboardingReducer.reduceSuggestionClickedMessage(
                state = state,
                activeCodeBlock = activeCodeBlock,
                newCodeBlock = newCodeBlock
            )

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
            is CodeBlock.IfStatement,
            is CodeBlock.ElifStatement -> {
                targetCodeBlock.children.mapIndexed { index, child ->
                    require(child is CodeBlockChild.SelectSuggestion)
                    if (index == message.codeBlockChildItem.id) {
                        child.copy(isActive = true)
                    } else {
                        child.copy(isActive = false)
                    }
                }
            }
            null,
            is CodeBlock.Blank,
            is CodeBlock.ElseStatement -> null
        }

        val newCodeBlocks = state.codeBlocks.mutate {
            newChildren?.let { newChildren ->
                state.activeCodeBlockIndex()?.let {
                    set(it, setCodeBlockIsActive(codeBlock = state.codeBlocks[it], isActive = false))
                }

                targetCodeBlock?.let { targetCodeBlock ->
                    set(
                        targetCodeBlockIndex,
                        targetCodeBlock.updatedChildren(newChildren)
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

        if (activeCodeBlock == null || activeCodeBlock.isDeleteForbidden) {
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
            val replaceActiveCodeBlockWithBlank = {
                set(
                    activeCodeBlockIndex,
                    CodeBlock.Blank(
                        isActive = true,
                        indentLevel = activeCodeBlock.indentLevel,
                        suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                            index = activeCodeBlockIndex,
                            indentLevel = activeCodeBlock.indentLevel,
                            codeBlocks = this,
                            isVariableSuggestionAvailable = state.isVariableSuggestionsAvailable,
                            availableConditions = state.availableConditions
                        )
                    )
                )
            }

            val isNextCodeBlockHasSameIndentLevelOrTrue = state.codeBlocks
                .getOrNull(activeCodeBlockIndex + 1)
                ?.let { it.indentLevel == activeCodeBlock.indentLevel }
                ?: true

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
                            replaceActiveCodeBlockWithBlank()
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
                                replaceActiveCodeBlockWithBlank()
                            }
                    }
                }
                is CodeBlock.IfStatement,
                is CodeBlock.ElifStatement -> {
                    val activeChildIndex = activeCodeBlock.activeChildIndex() ?: return@mutate
                    val activeChild = activeCodeBlock.children[activeChildIndex] as CodeBlockChild.SelectSuggestion

                    when {
                        activeChild.selectedSuggestion != null -> {
                            val newChildren = activeCodeBlock.children.mutate {
                                set(
                                    activeChildIndex,
                                    activeChild.copy(selectedSuggestion = null)
                                )
                            }
                            set(
                                activeCodeBlockIndex,
                                activeCodeBlock.updatedChildren(newChildren)
                            )
                        }

                        activeChildIndex > 0 -> {
                            val newChildren = activeCodeBlock.children.mutate {
                                val previousChildIndex = activeChildIndex - 1
                                val previousChild = this[previousChildIndex] as CodeBlockChild.SelectSuggestion
                                set(
                                    previousChildIndex,
                                    previousChild.copy(isActive = true)
                                )

                                removeAt(activeChildIndex)
                            }
                            set(
                                activeCodeBlockIndex,
                                activeCodeBlock.updatedChildren(newChildren)
                            )
                        }

                        (activeChildIndex == 0 || activeCodeBlock.areAllChildrenUnselected()) &&
                            isNextCodeBlockHasSameIndentLevelOrTrue -> {
                            if (state.codeBlocks.size > 1) {
                                removeActiveCodeBlockAndSetNextActive()
                            } else {
                                replaceActiveCodeBlockWithBlank()
                            }
                        }
                    }
                }
                is CodeBlock.ElseStatement -> {
                    if (isNextCodeBlockHasSameIndentLevelOrTrue) {
                        if (state.codeBlocks.size > 1) {
                            removeActiveCodeBlockAndSetNextActive()
                        } else {
                            replaceActiveCodeBlockWithBlank()
                        }
                    }
                }
            }
        }

        val (onboardingState, onboardingActions) =
            stepQuizCodeBlanksOnboardingReducer.reduceDeleteButtonClickedMessage(state)

        return state.copy(
            codeBlocks = newCodeBlocks,
            onboardingState = onboardingState
        ) to actions + onboardingActions
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
                    is CodeBlock.IfStatement,
                    is CodeBlock.ElifStatement,
                    is CodeBlock.ElseStatement -> activeCodeBlock.indentLevel + 1
                    else -> activeCodeBlock.indentLevel
                }

            val newCodeBlocks = state.codeBlocks.mutate {
                set(
                    activeCodeBlockIndex,
                    setCodeBlockIsActive(codeBlock = state.codeBlocks[activeCodeBlockIndex], isActive = false)
                )

                val insertIndex = activeCodeBlockIndex + 1
                add(
                    insertIndex,
                    CodeBlock.Blank(
                        isActive = true,
                        indentLevel = indentLevel,
                        suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                            index = insertIndex,
                            indentLevel = indentLevel,
                            codeBlocks = this,
                            isVariableSuggestionAvailable = state.isVariableSuggestionsAvailable,
                            availableConditions = state.availableConditions
                        )
                    )
                )
            }

            val (onboardingState, onboardingActions) =
                stepQuizCodeBlanksOnboardingReducer.reduceEnterButtonClickedMessage(state)

            state.copy(
                codeBlocks = newCodeBlocks,
                onboardingState = onboardingState
            ) to actions + onboardingActions
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
            is CodeBlock.IfStatement,
            is CodeBlock.ElifStatement -> {
                activeCodeBlock.activeChildIndex()?.let { activeChildIndex ->
                    val activeChild = activeCodeBlock.children[activeChildIndex] as CodeBlockChild.SelectSuggestion

                    val newChildSuggestions = when {
                        activeChild.selectedSuggestion?.isOpeningParentheses == true ->
                            state.variablesSuggestions + state.stringsSuggestions

                        activeChild.selectedSuggestion?.isClosingParentheses == true ->
                            state.operationsSuggestions

                        activeChild.selectedSuggestion in state.stringsSuggestions ||
                            activeChild.selectedSuggestion in state.variablesSuggestions ->
                            state.operationsSuggestions

                        activeChild.selectedSuggestion in state.operationsSuggestions ->
                            state.variablesSuggestions + state.stringsSuggestions

                        else -> state.operationsSuggestions + state.variablesAndStringsSuggestions
                    }

                    val newChild = CodeBlockChild.SelectSuggestion(
                        isActive = true,
                        suggestions = newChildSuggestions,
                        selectedSuggestion = null
                    )

                    activeCodeBlock.children.mutate {
                        set(activeChildIndex, activeChild.copy(isActive = false))
                        add(activeChildIndex + 1, newChild)
                    }
                }
            }
            is CodeBlock.Blank,
            is CodeBlock.ElseStatement -> null
        }

        val newCodeBlocks = state.codeBlocks.mutate {
            newChildren?.let {
                set(
                    activeCodeBlockIndex,
                    activeCodeBlock.updatedChildren(newChildren)
                )
            }
        }

        val (onboardingState, onboardingActions) =
            stepQuizCodeBlanksOnboardingReducer.reduceSpaceButtonClickedMessage(state)

        return state.copy(
            codeBlocks = newCodeBlocks,
            onboardingState = onboardingState
        ) to actions + onboardingActions
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
                        is CodeBlock.Blank -> activeCodeBlock.copy(
                            indentLevel = newIndentLevel,
                            suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                                index = activeCodeBlockIndex,
                                indentLevel = newIndentLevel,
                                codeBlocks = this,
                                isVariableSuggestionAvailable = state.isVariableSuggestionsAvailable,
                                availableConditions = state.availableConditions
                            )
                        )
                        else -> activeCodeBlock.updatedIndentLevel(newIndentLevel)
                    }
                )
            }
        ) to actions
    }

    private fun setCodeBlockIsActive(codeBlock: CodeBlock, isActive: Boolean): CodeBlock =
        when (codeBlock) {
            is CodeBlock.Blank -> codeBlock.copy(isActive = isActive)
            is CodeBlock.ElseStatement -> codeBlock.copy(isActive = isActive)
            is CodeBlock.Print,
            is CodeBlock.Variable,
            is CodeBlock.IfStatement,
            is CodeBlock.ElifStatement -> {
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
                        codeBlock.updatedChildren(newChildren)
                    }
                } else {
                    val newChildren = codeBlock.children.map { child ->
                        require(child is CodeBlockChild.SelectSuggestion)
                        child.copy(isActive = false)
                    }
                    codeBlock.updatedChildren(newChildren)
                }
            }
        }

    private fun createInitialCodeBlocks(step: Step): List<CodeBlock> {
        val templateCodeBlocks = CodeBlanksTemplateMapper.map(step)
        return templateCodeBlocks.ifEmpty {
            listOf(
                CodeBlock.Blank(
                    isActive = true,
                    indentLevel = 0,
                    suggestions = StepQuizCodeBlanksResolver.getSuggestionsForBlankCodeBlock(
                        isVariableSuggestionAvailable = StepQuizCodeBlanksResolver.isVariableSuggestionsAvailable(step),
                        availableConditions = step.block.options.codeBlanksAvailableConditions ?: emptySet()
                    )
                )
            )
        }
    }
}