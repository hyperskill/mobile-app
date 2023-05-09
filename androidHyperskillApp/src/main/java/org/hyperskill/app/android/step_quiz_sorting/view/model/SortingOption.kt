package org.hyperskill.app.android.step_quiz_sorting.view.model

import ru.nobird.app.core.model.Identifiable

data class SortingOption(
    override val id: Int,
    val option: String,
    val isEnabled: Boolean
) : Identifiable<Int>