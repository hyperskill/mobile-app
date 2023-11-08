package org.hyperskill.app.android.step_quiz_fill_blanks.model

import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksOption

data class FillBlanksSelectOptionUIItem(
    val option: FillBlanksOption,
    val isUsed: Boolean = false
)
