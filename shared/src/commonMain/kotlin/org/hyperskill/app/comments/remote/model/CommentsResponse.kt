package org.hyperskill.app.comments.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse

@Serializable
class CommentsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("comments")
    val comments: List<Comment>
) : MetaResponse