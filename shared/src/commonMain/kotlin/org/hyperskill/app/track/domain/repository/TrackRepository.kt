package org.hyperskill.app.track.domain.repository

import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackRepository {
    suspend fun getTrack(trackId: Long): Result<Track> =
        getTracks(listOf(trackId)).map { it.first() }

    suspend fun getTracks(trackIds: List<Long>): Result<List<Track>>

    suspend fun getTrackProgress(trackId: Long): Result<TrackProgress> =
        getTracksProgresses(listOf(trackId)).map { it.first() }

    suspend fun getTracksProgresses(trackIds: List<Long>): Result<List<TrackProgress>>
}