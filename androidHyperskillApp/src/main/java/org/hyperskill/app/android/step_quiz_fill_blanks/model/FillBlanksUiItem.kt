package org.hyperskill.app.android.step_quiz_fill_blanks.model

import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import ru.nobird.app.core.model.Identifiable

sealed interface FillBlanksUiItem : Identifiable<Int> {

    val origin: FillBlanksItem

    override val id: Int
        get() = origin.id

    data class Text(override val origin: FillBlanksItem.Text) : FillBlanksUiItem

    data class Input(override val origin: FillBlanksItem.Input) : FillBlanksUiItem {
        fun copy(inputText: String): Input =
            Input(origin.copy(inputText = inputText))
    }

    data class Select(
        override val origin: FillBlanksItem.Select,
        val isHighlighted: Boolean
    ) : FillBlanksUiItem {
        fun copy(
            selectedOptionIndex: Int?,
            isHighlighted: Boolean = this.isHighlighted
        ): Select =
            Select(
                origin.copy(selectedOptionIndex = selectedOptionIndex),
                isHighlighted
            )
    }
}