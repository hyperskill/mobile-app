package org.hyperskill.app.reactions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionsRequest(
    @SerialName("comment")
    val commentId: Long,
    @SerialName("short_name")
    val shortName: String
)