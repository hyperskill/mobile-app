package org.hyperskill.app.comments.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.comments.domain.model.Reaction
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse

@Serializable
class ReactionsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("reactions")
    val reactions: List<Reaction>
) : MetaResponse