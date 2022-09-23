package org.hyperskill.app.step.domain.model.serializer

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset

class DatasetSerializer : JsonTransformingSerializer<Dataset>(Dataset.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element !is JsonObject) JsonObject(emptyMap()) else element
}