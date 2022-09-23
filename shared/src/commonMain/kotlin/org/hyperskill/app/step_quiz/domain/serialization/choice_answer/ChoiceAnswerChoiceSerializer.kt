package org.hyperskill.app.step_quiz.domain.serialization.choice_answer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer

object ChoiceAnswerChoiceSerializer : KSerializer<ChoiceAnswer.Choice> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChoiceAnswer.Choice", PrimitiveKind.BOOLEAN)

    override fun serialize(encoder: Encoder, value: ChoiceAnswer.Choice): Unit =
        encoder.encodeBoolean(value.boolValue)

    override fun deserialize(decoder: Decoder): ChoiceAnswer.Choice =
        ChoiceAnswer.Choice(decoder.decodeBoolean())
}