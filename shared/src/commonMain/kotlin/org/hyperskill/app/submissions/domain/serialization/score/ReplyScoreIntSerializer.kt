package org.hyperskill.app.submissions.domain.serialization.score

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hyperskill.app.submissions.domain.model.ReplyScore

internal object ReplyScoreIntSerializer : KSerializer<ReplyScore.Int> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ReplyScore.Int", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: ReplyScore.Int) {
        encoder.encodeInt(value.intValue)
    }

    override fun deserialize(decoder: Decoder): ReplyScore.Int =
        ReplyScore.Int(decoder.decodeInt())
}