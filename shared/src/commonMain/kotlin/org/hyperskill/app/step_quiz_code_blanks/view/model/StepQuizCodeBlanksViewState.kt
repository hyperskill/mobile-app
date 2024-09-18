package org.hyperskill.app.step_quiz_code_blanks.view.model

import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState

sealed interface StepQuizCodeBlanksViewState {
    data object Idle : StepQuizCodeBlanksViewState

    data class Content(
        val codeBlocks: List<CodeBlockItem>,
        val suggestions: List<Suggestion>,
        val isDeleteButtonEnabled: Boolean,
        val isSpaceButtonHidden: Boolean,
        val isDecreaseIndentLevelButtonHidden: Boolean,
        internal val onboardingState: OnboardingState = OnboardingState.Unavailable
    ) : StepQuizCodeBlanksViewState {
        val isActionButtonsHidden: Boolean
            get() = onboardingState != OnboardingState.Unavailable

        val isSuggestionsHighlightEffectActive: Boolean
            get() = onboardingState == OnboardingState.PrintSuggestionAndCallToAction.HighlightSuggestions
    }

    sealed interface CodeBlockItem {
        val id: Int

        val indentLevel: Int

        val children: List<CodeBlockChildItem>

        data class Blank(
            override val id: Int,
            override val indentLevel: Int = 0,
            val isActive: Boolean
        ) : CodeBlockItem {
            override val children: List<CodeBlockChildItem> = emptyList()
        }

        data class Print(
            override val id: Int,
            override val indentLevel: Int = 0,
            override val children: List<CodeBlockChildItem>
        ) : CodeBlockItem

        data class Variable(
            override val id: Int,
            override val indentLevel: Int = 0,
            override val children: List<CodeBlockChildItem>
        ) : CodeBlockItem {
            val name: CodeBlockChildItem?
                get() = children.firstOrNull()

            val values: List<CodeBlockChildItem>
                get() = children.drop(1)
        }

        data class IfStatement(
            override val id: Int,
            override val indentLevel: Int = 0,
            override val children: List<CodeBlockChildItem>
        ) : CodeBlockItem

        data class ElifStatement(
            override val id: Int,
            override val indentLevel: Int = 0,
            override val children: List<CodeBlockChildItem>
        ) : CodeBlockItem

        data class ElseStatement(
            override val id: Int,
            override val indentLevel: Int = 0,
            val isActive: Boolean
        ) : CodeBlockItem {
            override val children: List<CodeBlockChildItem> = emptyList()
        }
    }

    data class CodeBlockChildItem(
        val id: Int,
        val isActive: Boolean,
        val value: String?
    )
}