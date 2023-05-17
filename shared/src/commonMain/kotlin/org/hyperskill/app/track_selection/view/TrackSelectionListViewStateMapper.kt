package org.hyperskill.app.track_selection.view

import kotlin.math.floor
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature

internal class TrackSelectionListViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val numbersFormatter: NumbersFormatter
) {
    fun map(state: TrackSelectionListFeature.State): TrackSelectionListFeature.ViewState =
        when (state) {
            TrackSelectionListFeature.State.Idle -> TrackSelectionListFeature.ViewState.Idle
            TrackSelectionListFeature.State.Loading -> TrackSelectionListFeature.ViewState.Loading
            TrackSelectionListFeature.State.NetworkError -> TrackSelectionListFeature.ViewState.Error
            is TrackSelectionListFeature.State.Content -> getContentViewState(state)
        }

    private fun getContentViewState(
        state: TrackSelectionListFeature.State.Content
    ): TrackSelectionListFeature.ViewState =
        TrackSelectionListFeature.ViewState.Content(
            selectedTrack = state.tracks
                .firstOrNull { it.track.id == state.selectedTrackId }
                ?.let(::mapTrackToViewStateTrack),
            tracks = state.tracks
                .filter { it.track.id != state.selectedTrackId }
                .sortedBy { it.trackProgress.rank }
                .map(::mapTrackToViewStateTrack)
        )

    private fun mapTrackToViewStateTrack(
        trackWithProgress: TrackWithProgress
    ): TrackSelectionListFeature.ViewState.Track =
        TrackSelectionListFeature.ViewState.Track(
            id = trackWithProgress.track.id,
            imageSource = trackWithProgress.track.cover?.takeIf { it.isNotBlank() },
            title = trackWithProgress.track.title,
            timeToComplete = getTimeToComplete(trackWithProgress.track.secondsToComplete),
            rating = numbersFormatter.formatProgressAverageRating(trackWithProgress.trackProgress.averageRating),
            isBeta = trackWithProgress.track.isBeta,
            isCompleted = trackWithProgress.track.isCompleted
        )

    private fun getTimeToComplete(secondsToComplete: Float?): String? {
        if (secondsToComplete == null || secondsToComplete <= 0) return null
        // TODO: replace with date formatter
        val hours = floor(secondsToComplete / 3600).toInt()
        return if (hours >= 1) {
            resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
        } else {
            null
        }
    }
}