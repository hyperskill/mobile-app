package org.hyperskill.app.submissions.domain.serialization.choice_answer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.hyperskill.app.submissions.domain.model.ChoiceAnswer

object ChoiceAnswerContentSerializer : JsonContentPolymorphicSerializer<ChoiceAnswer>(ChoiceAnswer::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ChoiceAnswer> =
        if (element is JsonObject) {
            ChoiceAnswer.Table.serializer()
        } else {
            ChoiceAnswer.Choice.serializer()
        }
}