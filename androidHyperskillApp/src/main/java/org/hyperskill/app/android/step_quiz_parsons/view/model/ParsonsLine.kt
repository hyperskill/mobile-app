package org.hyperskill.app.android.step_quiz_parsons.view.model

import ru.nobird.app.core.model.Identifiable

data class ParsonsLine(
    val lineNumber: Int,
    val text: String,
    val tabsCount: Int,
    val isSelected: Boolean
) : Identifiable<Int> {
    override val id: Int
        get() = lineNumber
}