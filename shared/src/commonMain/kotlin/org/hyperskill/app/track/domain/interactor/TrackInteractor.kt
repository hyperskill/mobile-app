package org.hyperskill.app.track.domain.interactor

import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.repository.TrackRepository

class TrackInteractor(
    private val trackRepository: TrackRepository
) {
    suspend fun getTrack(trackId: Long): Result<Track> =
        kotlin.runCatching {
            return trackRepository.getTrack(trackId)
        }

    suspend fun getAllTracks(): Result<List<Track>> =
        trackRepository.getAllTracks()

    suspend fun getStudyPlans(): Result<List<StudyPlan>> =
        trackRepository.getStudyPlans()

    suspend fun getStudyPlanByTrackId(trackId: Long): Result<StudyPlan> =
        kotlin.runCatching {
            return trackRepository.getStudyPlanByTrackId(trackId)
        }
}