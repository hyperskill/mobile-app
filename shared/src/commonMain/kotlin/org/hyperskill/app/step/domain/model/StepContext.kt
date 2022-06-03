package org.hyperskill.app.step.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StepContext {
    @SerialName("default")
    DEFAULT,
    @SerialName("diagnostic")
    DIAGNOSTIC,
    @SerialName("verification")
    VERIFICATION,
    @SerialName("repetition")
    REPETITION,
}