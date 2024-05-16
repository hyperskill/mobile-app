package org.hyperskill.app.android.step_quiz_fill_blanks.model

import ru.nobird.app.core.model.Identifiable

data class FillBlanksSelectOptionUIItem(
    override val id: Int,
    val option: FillBlanksProcessedOption,
    val isClickable: Boolean,
    val isUsed: Boolean = false
) : Identifiable<Int>