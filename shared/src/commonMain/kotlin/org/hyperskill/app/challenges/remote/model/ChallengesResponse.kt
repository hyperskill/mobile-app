package org.hyperskill.app.challenges.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse

@Serializable
class ChallengesResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("challenges")
    val challenges: List<Challenge>
) : MetaResponse