package org.hyperskill.app.submissions.domain.serialization.score

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hyperskill.app.submissions.domain.model.ReplyScore

internal object ReplyScoreFloatSerializer : KSerializer<ReplyScore.Float> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ReplyScore.Float", PrimitiveKind.FLOAT)

    override fun serialize(encoder: Encoder, value: ReplyScore.Float) {
        encoder.encodeFloat(value.floatValue)
    }

    override fun deserialize(decoder: Decoder): ReplyScore.Float =
        ReplyScore.Float(decoder.decodeFloat())
}