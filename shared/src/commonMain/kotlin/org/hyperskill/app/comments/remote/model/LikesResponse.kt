package org.hyperskill.app.comments.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.comments.domain.model.Like
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse

@Serializable
class LikesResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("likes")
    val likes: List<Like>
) : MetaResponse