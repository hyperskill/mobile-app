package org.hyperskill.app.android.step_quiz.model

import ru.nobird.app.core.model.Identifiable

data class Choice(
    val option: String,
    val correct: Boolean? = null,
    val feedback: String? = null,
    val isEnabled: Boolean = false
) : Identifiable<String> {
    override val id: String
        get() = option
}