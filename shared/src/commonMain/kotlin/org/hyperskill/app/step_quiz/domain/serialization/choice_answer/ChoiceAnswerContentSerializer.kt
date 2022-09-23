package org.hyperskill.app.step_quiz.domain.serialization.choice_answer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer

object ChoiceAnswerContentSerializer : JsonContentPolymorphicSerializer<ChoiceAnswer>(ChoiceAnswer::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ChoiceAnswer> =
        if (element is JsonObject) {
            ChoiceAnswer.Table.serializer()
        } else {
            ChoiceAnswer.Choice.serializer()
        }
}
