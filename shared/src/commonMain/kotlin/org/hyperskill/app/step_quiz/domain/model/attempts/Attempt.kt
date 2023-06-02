package org.hyperskill.app.step_quiz.domain.model.attempts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.step.domain.model.serializer.DatasetSerializer

@Serializable
data class Attempt(
    @SerialName("id")
    val id: Long,

    @SerialName("dataset")
    @Serializable(with = DatasetSerializer::class)
    val dataset: Dataset?,

    @SerialName("status")
    val status: AttemptStatus?
)