package org.hyperskill.app.track_selection.details.cache

import com.russhwolf.settings.Settings
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.hyperskill.app.core.utils.mutate
import org.hyperskill.app.track_selection.details.data.source.TrackSelectionDetailsCacheDataSource

@Serializable
private class TrackSelectionDetailsCountContainer(val map: Map<Long, Int>)

internal class TrackSelectionDetailsCacheDataSourceImpl(
    private val json: Json,
    private val settings: Settings
) : TrackSelectionDetailsCacheDataSource {
    override fun getTracksSelectionCountMap(): Map<Long, Int> =
        settings
            .getStringOrNull(TrackSelectionDetailsCacheKeyValues.TRACK_SELECTION_DETAILS_COUNT_MAP)
            ?.let { json.decodeFromString(TrackSelectionDetailsCountContainer.serializer(), it).map }
            ?: emptyMap()

    override fun incrementTrackSelectionCount(trackId: Long) {
        val updatedMap = getTracksSelectionCountMap().mutate {
            val current = get(trackId) ?: 0
            set(trackId, current + 1)
        }
        settings.putString(
            TrackSelectionDetailsCacheKeyValues.TRACK_SELECTION_DETAILS_COUNT_MAP,
            json.encodeToString(
                TrackSelectionDetailsCountContainer.serializer(),
                TrackSelectionDetailsCountContainer(updatedMap)
            )
        )
    }
}