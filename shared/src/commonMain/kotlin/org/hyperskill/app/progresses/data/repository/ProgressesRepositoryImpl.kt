package org.hyperskill.app.progresses.data.repository

import org.hyperskill.app.progresses.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

class ProgressesRepositoryImpl(
    private val progressesRemoteDataSource: ProgressesRemoteDataSource
) : ProgressesRepository {
    override suspend fun getTracksProgresses(tracksIds: List<Long>): Result<List<TrackProgress>> =
        progressesRemoteDataSource.getTracksProgresses(tracksIds)

    override suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>> =
        progressesRemoteDataSource.getTopicsProgresses(topicsIds)
}