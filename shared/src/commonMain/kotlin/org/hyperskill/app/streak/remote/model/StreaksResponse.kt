package org.hyperskill.app.streak.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.streak.domain.model.Streak

@Serializable
class StreaksResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("streaks")
    val streaks: List<Streak>
) : MetaResponse