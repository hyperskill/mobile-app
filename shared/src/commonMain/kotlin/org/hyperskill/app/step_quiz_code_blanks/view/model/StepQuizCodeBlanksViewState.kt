package org.hyperskill.app.step_quiz_code_blanks.view.model

import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import ru.nobird.app.core.model.Identifiable

sealed interface StepQuizCodeBlanksViewState {
    data object Idle : StepQuizCodeBlanksViewState

    data class Content(
        val codeBlocks: List<CodeBlockItem>,
        val suggestions: List<Suggestion>,
        val isDeleteButtonVisible: Boolean
    ) : StepQuizCodeBlanksViewState

    sealed class CodeBlockItem : Identifiable<Int> {
        abstract val isActive: Boolean

        data class Blank(
            override val id: Int,
            override val isActive: Boolean
        ) : CodeBlockItem()

        data class Print(
            override val id: Int,
            override val isActive: Boolean,
            val output: String?
        ) : CodeBlockItem()
    }
}