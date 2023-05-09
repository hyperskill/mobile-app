package org.hyperskill.app.step_quiz.domain.model.attempts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pair(
    @SerialName("first")
    val first: String?,
    @SerialName("second")
    val second: String?
)