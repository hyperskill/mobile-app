package org.hyperskill.app.comments.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentStatisticsEntry(
    @SerialName("thread")
    val thread: CommentThread = CommentThread.UNKNOWN,
    @SerialName("total_count")
    val totalCount: Int
)