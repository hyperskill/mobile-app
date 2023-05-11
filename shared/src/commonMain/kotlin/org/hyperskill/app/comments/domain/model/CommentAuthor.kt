package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentAuthor(
    @SerialName("id")
    val id: Long,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("badge_title")
    val badgeTitle: String,
    @SerialName("fullname")
    val fullName: String,
)