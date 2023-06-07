package org.hyperskill.app.step.domain.model

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
    USEFUL_LINK
}