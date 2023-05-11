package org.hyperskill.app.likes.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.likes.domain.model.LikeSubject

@Serializable
data class LikesRequest(
    @SerialName("target_type")
    val targetType: String,
    @SerialName("target_id")
    val targetId: Long,
    @SerialName("subject")
    val subject: LikeSubject,
    @SerialName("value")
    val value: Int
)