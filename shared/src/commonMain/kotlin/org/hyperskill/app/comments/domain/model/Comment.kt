package org.hyperskill.app.comments.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    @SerialName("id")
    val id: Long,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("target_type")
    val targetType: String,
    @SerialName("text")
    val text: String,
    @SerialName("localized_text")
    val localizedText: String,
    @SerialName("user")
    val user: CommentAuthor,
    @SerialName("time")
    val time: Instant? = null,
    @SerialName("reactions")
    val reactions: List<CommentReaction>
)