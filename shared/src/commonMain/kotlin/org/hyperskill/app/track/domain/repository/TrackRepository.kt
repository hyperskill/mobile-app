package org.hyperskill.app.track.domain.repository

import org.hyperskill.app.track.domain.model.Track

interface TrackRepository {
    suspend fun getTrack(trackId: Long, forceLoadFromRemote: Boolean): Result<Track> =
        kotlin.runCatching {
            getTracks(listOf(trackId), forceLoadFromRemote).getOrThrow().first()
        }

    suspend fun getTracks(trackIds: List<Long>, forceLoadFromRemote: Boolean): Result<List<Track>>

    suspend fun getAllTracks(): Result<List<Track>>

    fun clearCache()
}