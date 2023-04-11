package org.hyperskill.app.track.domain.interactor

import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.repository.TrackRepository

class TrackInteractor(
    private val trackRepository: TrackRepository
) {
    suspend fun getTrack(trackId: Long, forceLoadFromRemote: Boolean): Result<Track> =
        kotlin.runCatching {
            return trackRepository.getTrack(trackId, forceLoadFromRemote)
        }

    suspend fun getAllTracks(): Result<List<Track>> =
        trackRepository.getAllTracks()
}