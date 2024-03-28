package org.hyperskill.app.submissions.domain.serialization.feedback

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.hyperskill.app.submissions.domain.model.Feedback

object FeedbackContentSerializer : JsonContentPolymorphicSerializer<Feedback>(Feedback::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Feedback> =
        if (element is JsonObject) {
            Feedback.Object.serializer()
        } else {
            Feedback.Text.serializer()
        }
}