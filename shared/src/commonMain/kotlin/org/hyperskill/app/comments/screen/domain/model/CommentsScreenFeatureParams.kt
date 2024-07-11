package org.hyperskill.app.comments.screen.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry
import org.hyperskill.app.step.domain.model.StepRoute

@Serializable
data class CommentsScreenFeatureParams(
    val stepRoute: StepRoute,
    val commentStatistics: CommentStatisticsEntry
)