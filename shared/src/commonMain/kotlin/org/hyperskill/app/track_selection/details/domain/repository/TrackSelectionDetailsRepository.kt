package org.hyperskill.app.track_selection.details.domain.repository

interface TrackSelectionDetailsRepository {
    fun getTracksSelectionCountMap(): Map<Long, Int>
    fun incrementTrackSelectionCount(trackId: Long)
}