package org.hyperskill.app.reactions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.reactions.domain.model.Reaction

@Serializable
class ReactionsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("reactions")
    val reactions: List<Reaction>
) : MetaResponse