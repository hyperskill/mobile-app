package org.hyperskill.app.topics_repetitions.domain.model

data class TopicToRepeat(
    val topicId: Long,
    val title: String,
    val stepId: Long,
    val theory: Long
)
