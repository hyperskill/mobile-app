package org.hyperskill.app.step_quiz_code_blanks.view.model

import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState

sealed interface StepQuizCodeBlanksViewState {
    data object Idle : StepQuizCodeBlanksViewState

    data class Content(
        val codeBlocks: List<CodeBlockItem>,
        val suggestions: List<Suggestion>,
        val isDeleteButtonEnabled: Boolean,
        internal val onboardingState: OnboardingState = OnboardingState.Unavailable
    ) : StepQuizCodeBlanksViewState {
        val isActionButtonsHidden: Boolean
            get() = onboardingState != OnboardingState.Unavailable

        val isSuggestionsHighlightEffectActive: Boolean
            get() = onboardingState == OnboardingState.HighlightSuggestions
    }

    sealed interface CodeBlockItem {
        val id: Int

        val children: List<CodeBlockChildItem>

        data class Blank(
            override val id: Int,
            val isActive: Boolean
        ) : CodeBlockItem {
            override val children: List<CodeBlockChildItem> = emptyList()
        }

        data class Print(
            override val id: Int,
            override val children: List<CodeBlockChildItem>
        ) : CodeBlockItem {
            val isActive: Boolean
                get() = children.firstOrNull()?.isActive == true

            val output: String?
                get() = children.firstOrNull()?.value
        }

        data class Variable(
            override val id: Int,
            override val children: List<CodeBlockChildItem>
        ) : CodeBlockItem {
            val name: CodeBlockChildItem?
                get() = children.firstOrNull()

            val value: CodeBlockChildItem?
                get() = children.lastOrNull()
        }
    }

    data class CodeBlockChildItem(
        val id: Int,
        val isActive: Boolean,
        val value: String?
    )
}