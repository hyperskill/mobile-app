package org.hyperskill.app.submissions.domain.serialization.choice_answer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hyperskill.app.submissions.domain.model.ChoiceAnswer
import org.hyperskill.app.submissions.domain.model.TableChoiceAnswer

object ChoiceAnswerTableSerializer : KSerializer<ChoiceAnswer.Table> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ChoiceAnswer.Table")

    override fun serialize(encoder: Encoder, value: ChoiceAnswer.Table): Unit =
        encoder.encodeSerializableValue(TableChoiceAnswer.serializer(), value.tableChoice)

    override fun deserialize(decoder: Decoder): ChoiceAnswer.Table =
        ChoiceAnswer.Table(decoder.decodeSerializableValue(TableChoiceAnswer.serializer()))
}