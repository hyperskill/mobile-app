package org.hyperskill.app.step_quiz_fill_blanks.model

import ru.nobird.app.core.model.Identifiable

sealed interface FillBlanksItem : Identifiable<Int> {
    /**
     * Represents a textual fill-in-the-blank item in a quiz.
     *
     * @param id The order number in a list of items.
     * @param text The content of the fill-in-the-blank text.
     *
     * @param startsWithNewLine Indicates whether the text item should start with a new line preventive.
     * If true, then the '\n' was placed before the text.
     */
    data class Text(
        override val id: Int,
        val text: String,
        val startsWithNewLine: Boolean
    ) : FillBlanksItem

    /**
     * Represents an input fill-in-the-blank item in a quiz.
     *
     * @param id The order number in a list of items.
     * @param inputText The input text provided by the user.
     */
    data class Input(
        override val id: Int,
        val inputText: String?
    ) : FillBlanksItem

    /**
     * Represents a select fill-in-the-blank item in a quiz.
     *
     * @property id The order number in a list of items.
     * @property selectedOptionIndex The order number in a list of options. Null if no option is selected.
     *
     * @see FillBlanksData.options
     */
    data class Select(
        override val id: Int,
        val selectedOptionIndex: Int?
    ) : FillBlanksItem
}