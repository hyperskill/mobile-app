package org.hyperskill.app.step_quiz_hints.domain.model

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.hyperskill.app.user_storage.domain.model.UserStorageValue

enum class HintState(val userStorageValue: UserStorageValue) {
    SEEN(JsonPrimitive("seen")),
    HELPFUL(JsonPrimitive("helpful")),
    UNHELPFUL(JsonPrimitive("unhelpful"));

    companion object {
        private val VALUES: Array<HintState> = values()

        fun getByStringValue(value: String): HintState? =
            VALUES.firstOrNull { it.userStorageValue.jsonPrimitive.content == value }
    }

    val hasReaction: Boolean
        get() = when (this) {
            SEEN -> false
            HELPFUL -> true
            UNHELPFUL -> true
        }
}