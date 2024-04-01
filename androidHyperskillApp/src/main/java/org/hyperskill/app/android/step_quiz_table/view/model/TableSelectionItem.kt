package org.hyperskill.app.android.step_quiz_table.view.model

import org.hyperskill.app.submissions.domain.model.Cell
import ru.nobird.app.core.model.Identifiable

data class TableSelectionItem(
    override val id: Int,
    val titleText: String,
    val tableChoices: List<Cell>,
    val isEnabled: Boolean
) : Identifiable<Int>