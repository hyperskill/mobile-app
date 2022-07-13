package org.hyperskill.app.track.data.source

import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackRemoteDataSource {
    suspend fun getTracks(trackIds: List<Long>): Result<List<Track>>
    suspend fun getTracksProgresses(trackIds: List<Long>): Result<List<TrackProgress>>
    suspend fun getStudyPlans(): Result<List<StudyPlan>>
}