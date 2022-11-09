package org.hyperskill.app.comments.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikesRequest(
    @SerialName("target_type")
    val targetType: String,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("subject")
    val subject: String,
    @SerialName("value")
    val value: Int
)
