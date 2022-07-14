package org.hyperskill.app.step.domain.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hyperskill.app.step.domain.model.Sample

class SampleSerializer : KSerializer<Sample> {
    override fun deserialize(decoder: Decoder): Sample {
        val listWithSample = decoder.decodeSerializableValue(ListSerializer(String.serializer()))

        return Sample(listWithSample.first(), listWithSample.last())
    }

    override val descriptor: SerialDescriptor = ListSerializer(String.serializer()).descriptor

    override fun serialize(encoder: Encoder, value: Sample) {
        encoder.encodeSerializableValue(Sample.serializer(), value)
    }
}