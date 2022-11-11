package org.hyperskill.app.likes.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.likes.domain.model.Like

@Serializable
class LikesResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("likes")
    val likes: List<Like>
) : MetaResponse