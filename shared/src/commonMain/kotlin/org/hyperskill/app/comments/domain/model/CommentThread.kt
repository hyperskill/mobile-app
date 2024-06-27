package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CommentThread {
    @SerialName("comment")
    COMMENT,
    @SerialName("solutions")
    SOLUTIONS,
    @SerialName("hint")
    HINT,
    @SerialName("useful link")
    USEFUL_LINK,

    UNKNOWN
}