package org.hyperskill.app.step_quiz_hints.domain.model

import kotlinx.serialization.json.JsonPrimitive
import org.hyperskill.app.user_storage.domain.model.UserStorageValue

enum class HintState(val userStorageValue: UserStorageValue) {
    SEEN(JsonPrimitive("seen")),
    HELPFUL(JsonPrimitive("helpful")),
    UNHELPFUL(JsonPrimitive("unhelpful")),
}