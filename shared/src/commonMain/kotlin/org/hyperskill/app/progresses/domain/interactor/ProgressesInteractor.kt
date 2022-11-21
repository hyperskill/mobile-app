package org.hyperskill.app.progresses.domain.interactor

import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

class ProgressesInteractor(
    private val progressesRepository: ProgressesRepository
) {
    suspend fun getTracksProgresses(tracksIds: List<Long>): Result<List<TrackProgress>> =
        progressesRepository.getTracksProgresses(tracksIds)

    suspend fun getTrackProgress(trackId: Long): Result<TrackProgress?> =
        progressesRepository.getTrackProgress(trackId)

    suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>> =
        progressesRepository.getTopicsProgresses(topicsIds)
}