package org.hyperskill.app.step.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Limit(
    @SerialName("time")
    val time: Int,
    @SerialName("memory")
    val memory: Int
)