package org.hyperskill.app.android.step_quiz_fill_blanks.model

data class FillBlanksSelectOptionUIItem(
    val option: FillBlanksProcessedOption,
    val isClickable: Boolean,
    val isUsed: Boolean = false
)