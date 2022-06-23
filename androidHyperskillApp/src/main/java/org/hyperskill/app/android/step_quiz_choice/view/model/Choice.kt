package org.hyperskill.app.android.step_quiz_choice.view.model

import ru.nobird.app.core.model.Identifiable

data class Choice(
    val option: String,
    val isSelected: Boolean = false,
    val isEnabled: Boolean = false
) : Identifiable<String> {
    override val id: String
        get() = option
}