package org.hyperskill.app.track_selection.details.data.source

interface TrackSelectionDetailsCacheDataSource {
    fun getTracksSelectionCountMap(): Map<Long, Int>
    fun incrementTrackSelectionCount(trackId: Long)
}