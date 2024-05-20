package org.hyperskill.app.android.step_quiz_table.view.model

import ru.nobird.app.core.model.Identifiable

data class TableSelectionItem(
    override val id: Int,
    val titleText: String,
    val tableChoices: List<TableChoiceItem>,
    val isEnabled: Boolean
) : Identifiable<Int>