package org.hyperskill.app.track_selection.details.data.repository

import org.hyperskill.app.track_selection.details.data.source.TrackSelectionDetailsCacheDataSource
import org.hyperskill.app.track_selection.details.domain.repository.TrackSelectionDetailsRepository

internal class TrackSelectionDetailsRepositoryImpl(
    private val trackSelectionDetailsCacheDataSource: TrackSelectionDetailsCacheDataSource
) : TrackSelectionDetailsRepository {
    override fun getTracksSelectionCountMap(): Map<Long, Int> =
        trackSelectionDetailsCacheDataSource.getTracksSelectionCountMap()

    override fun incrementTrackSelectionCount(trackId: Long) {
        trackSelectionDetailsCacheDataSource.incrementTrackSelectionCount(trackId)
    }
}