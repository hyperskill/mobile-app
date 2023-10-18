package org.hyperskill.app.step_quiz_fill_blanks.model

import ru.nobird.app.core.model.Identifiable

sealed interface FillBlanksItem : Identifiable<Int> {
    data class Text(
        override val id: Int,
        val text: String,
        val startsWithNewLine: Boolean
    ) : FillBlanksItem

    data class Input(
        override val id: Int,
        val inputText: String?
    ) : FillBlanksItem
}