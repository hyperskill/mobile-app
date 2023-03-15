package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TargetStateSerializer : KSerializer<TargetState> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("TargetState", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): TargetState =
        TargetState.valueOf(decoder.decodeInt())

    override fun serialize(encoder: Encoder, value: TargetState) {
        encoder.encodeInt(value.intValue)
    }
}