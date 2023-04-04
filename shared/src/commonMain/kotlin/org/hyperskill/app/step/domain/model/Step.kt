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
    @SerialName("check_profile")
    val checkProfile: String = "",
    @SerialName("seconds_to_complete")
    val secondsToComplete: Float?,
    @SerialName("last_completed_at")
    val lastCompletedAt: Instant? = null
) {
    @Serializable
    enum class Type {
        @SerialName("theory")
        THEORY,
        @SerialName("practice")
        PRACTICE
    }

    val millisSinceLastCompleted: Long?
        get() = lastCompletedAt?.let { (Clock.System.now() - it).inWholeMilliseconds }
}

/**
 * pycharm step has an IDE requirement if visible_files_count > 1 OR
 * visible_files_count <= 1 and check_profile is empty
 */
internal fun Step.isIdeRequired(): Boolean {
    if (block.name != BlockName.PYCHARM) {
        return false
    }

    val visibleFilesCount = block.options.files?.count { it.isVisible } ?: 0

    return visibleFilesCount > 1 || (visibleFilesCount <= 1 && checkProfile.isEmpty())
}

fun Step.pycharmCode(): String? =
    block.options.files?.first { it.isVisible }?.text