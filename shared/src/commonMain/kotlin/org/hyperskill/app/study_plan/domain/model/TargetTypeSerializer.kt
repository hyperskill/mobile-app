package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TargetTypeSerializer : KSerializer<TargetType> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("TargetType", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): TargetType =
        TargetType.valueOf(decoder.decodeInt())

    override fun serialize(encoder: Encoder, value: TargetType) {
        encoder.encodeInt(value.intValue)
    }
}