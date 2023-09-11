package org.hyperskill.app.android.step_quiz_parsons.view.model

import android.text.Spannable
import ru.nobird.app.core.model.Identifiable

data class UiParsonsLine(
    val lineNumber: Int,
    val formattedText: Spannable,
    val originText: String,
    val langName: String,
    val tabsCount: Int,
    val isSelected: Boolean,
    val isClickable: Boolean
) : Identifiable<Int> {
    override val id: Int
        get() = lineNumber
}