package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.reactions.domain.model.ReactionType

@Serializable
data class CommentReaction(
    @SerialName("short_name")
    val reactionType: ReactionType = ReactionType.UNKNOWN,
    @SerialName("value")
    val value: Int,
    @SerialName("is_set")
    val isSet: Boolean
)