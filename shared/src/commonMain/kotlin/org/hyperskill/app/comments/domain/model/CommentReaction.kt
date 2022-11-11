package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentReaction(
    @SerialName("short_name")
    val shortName: String,
    @SerialName("value")
    val value: Long,
    @SerialName("is_set")
    val isSet: Boolean,
)
