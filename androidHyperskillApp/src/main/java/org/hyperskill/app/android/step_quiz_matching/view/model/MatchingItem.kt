package org.hyperskill.app.android.step_quiz_matching.view.model

import ru.nobird.app.core.model.Identifiable

sealed class MatchingItem : Identifiable<Int> {
    data class Title(
        override val id: Int,
        val text: String,
        val isEnabled: Boolean
    ) : MatchingItem()

    data class Option(
        override val id: Int,
        val text: String,
        val isEnabled: Boolean
    ) : MatchingItem()
}