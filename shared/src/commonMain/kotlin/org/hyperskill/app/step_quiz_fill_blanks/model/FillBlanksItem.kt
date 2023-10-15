package org.hyperskill.app.step_quiz_fill_blanks.model

sealed interface FillBlanksItem {
    data class Text(
        val text: String
    ) : FillBlanksItem

    data class Input(
        val inputText: String?
    ) : FillBlanksItem
}