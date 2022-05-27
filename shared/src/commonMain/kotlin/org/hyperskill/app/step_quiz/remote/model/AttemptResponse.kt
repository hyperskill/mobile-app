package org.hyperskill.app.step_quiz.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt

@Serializable
class AttemptResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("attempts")
    val attempts: List<Attempt>
) : MetaResponse