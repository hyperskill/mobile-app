package org.hyperskill.app.progresses.domain.repository

import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

interface ProgressesRepository {
    suspend fun getTracksProgresses(tracksIds: List<Long>): Result<List<TrackProgress>>

    suspend fun getTrackProgress(trackId: Long): Result<TrackProgress?> =
        getTracksProgresses(listOf(trackId)).map { it.firstOrNull() }

    suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>>

    suspend fun getTopicProgress(topicId: Long): Result<TopicProgress> =
        kotlin.runCatching {
            getTopicsProgresses(listOf(topicId)).getOrThrow().first()
        }
}