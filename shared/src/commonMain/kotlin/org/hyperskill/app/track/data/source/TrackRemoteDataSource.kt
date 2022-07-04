package org.hyperskill.app.track.data.source

import org.hyperskill.app.track.domain.model.Track

interface TrackRemoteDataSource {
    suspend fun getTracks(trackIds: List<Long>): Result<List<Track>>
}