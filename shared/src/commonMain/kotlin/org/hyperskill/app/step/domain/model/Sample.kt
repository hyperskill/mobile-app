package org.hyperskill.app.step.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.step.domain.model.serializer.SampleSerializer

@Serializable(with = SampleSerializer::class)
data class Sample(
    val input: String,
    val output: String
)
