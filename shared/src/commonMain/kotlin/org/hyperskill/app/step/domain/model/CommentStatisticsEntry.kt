package org.hyperskill.app.step.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentStatisticsEntry(
    @SerialName("thread")
    val thread: String,
    @SerialName("total_count")
    val totalCount: Int
)