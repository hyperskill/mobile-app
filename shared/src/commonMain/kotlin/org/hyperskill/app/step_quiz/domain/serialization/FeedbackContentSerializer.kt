package org.hyperskill.app.step_quiz.domain.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.hyperskill.app.step_quiz.domain.model.submissions.Feedback

object FeedbackContentSerializer : JsonContentPolymorphicSerializer<Feedback>(Feedback::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Feedback> =
        if (element is JsonObject) {
            Feedback.Object.serializer()
        } else {
            Feedback.Text.serializer()
        }
}