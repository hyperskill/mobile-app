package org.hyperskill.app.step.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Step(
    @SerialName("id")
    val id: Long,
    @SerialName("stepik_id")
    val stepikId: Long,
    @SerialName("lesson_stepik_id")
    val lessonStepikId: Long,
    @SerialName("position")
    val position: Int,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: Type,
    @SerialName("block")
    val block: Block,
    @SerialName("topic")
    val topic: Long,
    @SerialName("topic_theory")
    val topicTheory: Long,
    @SerialName("can_abandon")
    val canAbandon: Boolean,
    @SerialName("can_skip")
    val canSkip: Boolean,
    @SerialName("comments_statistics")
    val commentsStatistics: List<CommentStatisticsEntry>,
    @SerialName("content_created_at")
    val contentCreatedAt: Instant,
    @SerialName("content_updated_at")
    val contentUpdatedAt: Instant,
    @SerialName("solved_by")
    val solvedBy: Int,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("seconds_to_complete")
    val secondsToComplete: Float?
) {
    @Serializable
    enum class Type {
        @SerialName("theory")
        THEORY,
        @SerialName("practice")
        PRACTICE
    }
}
