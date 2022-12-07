package org.hyperskill.app.track.data.repository

import org.hyperskill.app.track.data.source.TrackRemoteDataSource
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.repository.TrackRepository

class TrackRepositoryImpl(
    private val trackRemoteDataSource: TrackRemoteDataSource
) : TrackRepository {
    override suspend fun getTracks(trackIds: List<Long>?): Result<List<Track>> =
        trackRemoteDataSource.getTracks(trackIds)

    override suspend fun getStudyPlans(): Result<List<StudyPlan>> =
        trackRemoteDataSource.getStudyPlans()
}