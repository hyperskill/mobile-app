package org.hyperskill.app.android.step_quiz_parsons.view.model

import android.text.Spanned
import ru.nobird.app.core.model.Identifiable

data class UiParsonsLine(
    val lineNumber: Int,
    val text: Spanned,
    val tabsCount: Int,
    val isSelected: Boolean,
    val isClickable: Boolean
) : Identifiable<Int> {
    override val id: Int
        get() = lineNumber
}