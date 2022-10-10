package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    @SerialName("id")
    val id: Long,
    @SerialName("text")
    val text: String,
    @SerialName("localized_text")
    val localizedText: String,
    @SerialName("time")
    val time: String,
    @SerialName("user")
    val user: CommentAuthor,
    @SerialName("user_role")
    val userRole: String,
    @SerialName("reactions")
    val reactions: List<CommentReaction>,
)
