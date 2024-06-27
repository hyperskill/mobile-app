package org.hyperskill.app.comments.screen.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry

@Serializable
data class CommentsScreenFeatureParams(
    val stepId: Long,
    val commentStatistics: CommentStatisticsEntry
)