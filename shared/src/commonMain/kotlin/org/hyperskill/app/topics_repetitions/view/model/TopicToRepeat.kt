package org.hyperskill.app.topics_repetitions.view.model

data class TopicToRepeat(
    val topicId: Long,
    val title: String,
    val stepId: Long,
    val theoryId: Long?,
    val repeatedCount: Int
)
