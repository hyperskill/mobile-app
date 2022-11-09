package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Like(
    @SerialName("id")
    val id: Long,
    @SerialName("subject")
    val subject: String,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("target_type")
    val targetType: String,
    @SerialName("value")
    val value: Int,
    @SerialName("user")
    val userId: Long
)