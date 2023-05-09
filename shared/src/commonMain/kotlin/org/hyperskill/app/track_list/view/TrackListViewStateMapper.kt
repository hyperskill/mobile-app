package org.hyperskill.app.track_list.view

import kotlin.math.floor
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_list.presentation.TrackListFeature

class TrackListViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: TrackListFeature.State): TrackListViewState =
        when (state) {
            TrackListFeature.State.Idle -> TrackListViewState.Idle
            TrackListFeature.State.Loading -> TrackListViewState.Loading
            TrackListFeature.State.NetworkError -> TrackListViewState.Error
            is TrackListFeature.State.Content -> TrackListViewState.Content(
                tracks = state.tracksWithProgresses.sortedBy { it.trackProgress.rank }
                    .map {
                        mapTrackToViewStateTrack(it, state.selectedTrackId)
                    }
            )
        }

    private fun mapTrackToViewStateTrack(trackWithProgress: TrackWithProgress, selectedTrackId: Long?): TrackListViewState.Track =
        TrackListViewState.Track(
            id = trackWithProgress.track.id,
            imageSource = trackWithProgress.track.cover ?: "",
            title = trackWithProgress.track.title,
            description = trackWithProgress.track.description,
            timeToComplete = getTimeToComplete(trackWithProgress.track.secondsToComplete),
            rating = trackWithProgress.trackProgress.averageRating.toString(),
            isSelected = trackWithProgress.track.id == selectedTrackId
        )

    private fun getTimeToComplete(secondsToComplete: Double): String {
        val hours = floor(secondsToComplete / 3600).toInt()

        return resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
    }
}