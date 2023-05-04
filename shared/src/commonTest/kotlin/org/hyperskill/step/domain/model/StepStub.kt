package org.hyperskill.step.domain.model

import kotlinx.datetime.Instant
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.CommentStatisticsEntry
import org.hyperskill.app.step.domain.model.Step

fun Step.Companion.stub(
    id: Long,
    title: String = "",
    type: Step.Type = Step.Type.PRACTICE,
    block: Block = Block.stub(),
    topicTheory: Long? = null,
    topic: Long? = null,
    commentsStatistics: List<CommentStatisticsEntry> = emptyList(),
    solvedBy: Int = 0,
    isCompleted: Boolean = false,
    isNext: Boolean = false,
    canSkip: Boolean = false,
    checkProfile: String = "",
    secondsToComplete: Float? = null,
    lastCompletedAt: Instant? = null
): Step =
    Step(
        id = id,
        title = title,
        type = type,
        block = block,
        topicTheory = topicTheory,
        topic = topic,
        commentsStatistics = commentsStatistics,
        solvedBy = solvedBy,
        isCompleted = isCompleted,
        isNext = isNext,
        canSkip = canSkip,
        checkProfile = checkProfile,
        secondsToComplete = secondsToComplete,
        lastCompletedAt = lastCompletedAt
    )
