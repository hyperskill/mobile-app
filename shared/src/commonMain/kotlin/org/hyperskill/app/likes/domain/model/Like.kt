package org.hyperskill.app.likes.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Like(
    @SerialName("id")
    val id: Long,
    @SerialName("subject")
    val subject: LikeSubject,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("target_type")
    val targetType: String,
    @SerialName("value")
    val value: Int,
    @SerialName("user")
    val userId: Long
)