package org.hyperskill.app.progresses.data.source

import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

interface ProgressesRemoteDataSource {
    suspend fun getTracksProgresses(tracksIds: List<Long>): Result<List<TrackProgress>>
    suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>>
}