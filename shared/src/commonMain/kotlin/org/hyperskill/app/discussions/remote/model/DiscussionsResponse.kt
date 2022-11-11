package org.hyperskill.app.discussions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.discussions.domain.model.Discussion

@Serializable
class DiscussionsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("discussions")
    val discussions: List<Discussion>
) : MetaResponse