package org.hyperskill.app.step.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Step(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: Type,
    @SerialName("block")
    val block: Block,
    @SerialName("topic_theory")
    val topicTheory: Long? = null,
    @SerialName("topic")
    val topic: Long? = null,
    @SerialName("comments_statistics")
    val commentsStatistics: List<CommentStatisticsEntry>,
    @SerialName("solved_by")
    val solvedBy: Int,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("is_next")
    val isNext: Boolean,
    @SerialName("can_skip")
    val canSkip: Boolean,
    @SerialName("seconds_to_complete")
    val secondsToComplete: Float?,
    @SerialName("last_completed_at")
    val lastCompletedAt: Instant
) {
    @Serializable
    enum class Type {
        @SerialName("theory")
        THEORY,
        @SerialName("practice")
        PRACTICE
    }

    val millisSinceLastCompleted: Long
        get() = (Clock.System.now() - lastCompletedAt).inWholeMilliseconds
}
