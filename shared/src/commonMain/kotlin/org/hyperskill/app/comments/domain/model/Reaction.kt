package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reaction(
    @SerialName("id")
    val id: Long,
    @SerialName("user")
    val userId: Long,
    @SerialName("comment")
    val commentId: Long,
    @SerialName("short_name")
    val shortName: String
)