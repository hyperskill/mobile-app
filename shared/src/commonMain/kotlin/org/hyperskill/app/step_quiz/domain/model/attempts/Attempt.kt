package org.hyperskill.app.step_quiz.domain.model.attempts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.step.domain.model.serializer.DatasetSerializer

@Serializable
data class Attempt(
    @SerialName("id")
    val id: Long,
    @SerialName("step")
    val step: Long,
    @SerialName("user")
    val user: Long,

    @SerialName("dataset")
    @Serializable(with = DatasetSerializer::class)
    val dataset: Dataset?,

    @SerialName("status")
    val status: AttemptStatus?,
    @SerialName("time")
    val time: String,

    @SerialName("time_left")
    val timeLeft: String?
)