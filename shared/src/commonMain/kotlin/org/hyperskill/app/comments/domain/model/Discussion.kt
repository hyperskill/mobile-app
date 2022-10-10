package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Discussion(
    @SerialName("id")
    val id: Long,
    @SerialName("comments")
    val comments: List<Long>,
)
