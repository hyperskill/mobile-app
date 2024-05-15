package org.hyperskill.app.android.step_quiz_fill_blanks.model

import android.text.Spanned

data class FillBlanksProcessedOption(
    val originalText: String,
    val displayText: Spanned
)