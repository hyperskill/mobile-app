package org.hyperskill.app.step_quiz.domain.serialization.feedback

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hyperskill.app.step_quiz.domain.model.submissions.Feedback

object FeedbackTextSerializer : KSerializer<Feedback.Text> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Feedback.Text", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Feedback.Text): Unit =
        encoder.encodeString(value.text)

    override fun deserialize(decoder: Decoder): Feedback.Text =
        Feedback.Text(decoder.decodeString())
}