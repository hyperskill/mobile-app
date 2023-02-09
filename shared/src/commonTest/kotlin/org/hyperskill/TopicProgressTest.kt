package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.topics.domain.model.topicId

class TopicProgressTest {
    @Test
    fun testTopicProgressGetTopicId() {
        val topicProgress = TopicProgress(
            id = "2-123",
            vid = "topic-123",
            isCompleted = false,
            isSkipped = false,
            isInCurrentTrack = true
        )
        assertEquals(123L, topicProgress.topicId)
    }

    @Test
    fun testTopicProgressGetTopicIdNull() {
        val topicProgresses = listOf(
            TopicProgress(
                id = "2-abc",
                vid = "topic-abc",
                isCompleted = false,
                isSkipped = false,
                isInCurrentTrack = true
            ),
            TopicProgress(
                id = "2-abc",
                vid = "abc-defog",
                isCompleted = false,
                isSkipped = false,
                isInCurrentTrack = true
            ),
            TopicProgress(
                id = "1-abc",
                vid = "",
                isCompleted = false,
                isSkipped = false,
                isInCurrentTrack = true
            ),
            TopicProgress(
                id = "1-abc",
                vid = "qwerty",
                isCompleted = false,
                isSkipped = false,
                isInCurrentTrack = true
            )
        )
        topicProgresses.forEach {
            assertEquals(null, it.topicId)
        }
    }
}