package org.hyperskill.app.submissions.domain.serialization.score

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.hyperskill.app.submissions.domain.model.ReplyScore

internal object ReplyScoreContentSerializer : JsonContentPolymorphicSerializer<ReplyScore>(ReplyScore::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ReplyScore> {
        require(element is JsonPrimitive)
        return when {
            element.isString -> ReplyScore.String.serializer()
            else -> ReplyScore.Float.serializer()
        }
    }
}