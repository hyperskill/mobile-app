package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TargetTypeSerializer : KSerializer<LearningActivityType> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("TargetType", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): LearningActivityType =
        LearningActivityType.valueOf(decoder.decodeInt())

    override fun serialize(encoder: Encoder, value: LearningActivityType) {
        encoder.encodeInt(value.intValue)
    }
}