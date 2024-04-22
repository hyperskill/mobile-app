package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.topics.domain.model.topicId

class TopicProgressTest {
    @Test
    fun testTopicProgressGetTopicId() {
        val topicProgress = TopicProgress(
            vid = "topic-123",
            isCompleted = false,
            isSkipped = false
        )
        assertEquals(123L, topicProgress.topicId)
    }

    @Test
    fun testTopicProgressGetTopicIdNull() {
        val topicProgresses = listOf(
            TopicProgress(
                vid = "topic-abc",
                isCompleted = false,
                isSkipped = false
            ),
            TopicProgress(
                vid = "abc-defog",
                isCompleted = false,
                isSkipped = false
            ),
            TopicProgress(
                vid = "",
                isCompleted = false,
                isSkipped = false
            ),
            TopicProgress(
                vid = "qwerty",
                isCompleted = false,
                isSkipped = false
            )
        )
        topicProgresses.forEach {
            assertEquals(null, it.topicId)
        }
    }
}