package org.hyperskill.app.android.step_quiz_fill_blanks.model

import android.text.Spanned

/**
 * Represents a processed option for the fill in the blanks quiz.
 *
 * @property originalText The original text of the option.
 * @property displayText The option text ready to be displayed. All the HTML tags are presented as [Spanned] string.
 */
data class FillBlanksProcessedOption(
    val originalText: String,
    val displayText: Spanned
)