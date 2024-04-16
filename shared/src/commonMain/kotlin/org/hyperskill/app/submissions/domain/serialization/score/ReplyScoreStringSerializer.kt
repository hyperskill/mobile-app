package org.hyperskill.app.submissions.domain.serialization.score

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hyperskill.app.submissions.domain.model.ReplyScore

internal object ReplyScoreStringSerializer : KSerializer<ReplyScore.String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ReplyScore.String", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ReplyScore.String) {
        encoder.encodeString(value.stringValue)
    }

    override fun deserialize(decoder: Decoder): ReplyScore.String =
        ReplyScore.String(decoder.decodeString())
}