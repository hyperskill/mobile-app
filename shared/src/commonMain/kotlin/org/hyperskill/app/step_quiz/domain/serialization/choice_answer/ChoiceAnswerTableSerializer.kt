package org.hyperskill.app.step_quiz.domain.serialization.choice_answer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer
import org.hyperskill.app.step_quiz.domain.model.submissions.TableChoiceAnswer

object ChoiceAnswerTableSerializer : KSerializer<ChoiceAnswer.Table> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ChoiceAnswer.Table")

    override fun serialize(encoder: Encoder, value: ChoiceAnswer.Table): Unit =
        encoder.encodeSerializableValue(TableChoiceAnswer.serializer(), value.tableChoice)

    override fun deserialize(decoder: Decoder): ChoiceAnswer.Table =
        ChoiceAnswer.Table(decoder.decodeSerializableValue(TableChoiceAnswer.serializer()))
}