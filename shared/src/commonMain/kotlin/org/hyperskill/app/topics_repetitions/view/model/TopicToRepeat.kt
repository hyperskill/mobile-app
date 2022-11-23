package org.hyperskill.app.topics_repetitions.view.model

data class TopicToRepeat(
    val topicId: Long,
    val title: String,
    val stepId: Long,
    val repeatedCount: Int
)
