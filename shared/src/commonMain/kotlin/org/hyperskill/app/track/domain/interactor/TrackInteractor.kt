package org.hyperskill.app.track.domain.interactor

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
}