package org.hyperskill.app.track.domain.repository

import org.hyperskill.app.track.domain.model.Track

interface TrackRepository {
    suspend fun getTrack(trackId: Long): Result<Track> =
        getTracks(listOf(trackId)).map { it.first() }

    suspend fun getTracks(trackIds: List<Long>): Result<List<Track>>
}