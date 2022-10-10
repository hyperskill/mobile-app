package org.hyperskill.app.comments.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.comments.domain.model.Discussion
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse

@Serializable
class DiscussionsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("discussions")
    val discussions: List<Discussion>
) : MetaResponse