package org.hyperskill.app.android.step_quiz_table.view.model

import kotlinx.serialization.Serializable
import ru.nobird.app.core.model.Identifiable

@Serializable
data class TableChoiceItem(
    override val id: Int,
    val text: String,
    val answer: Boolean
) : Identifiable<Int>