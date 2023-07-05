package org.hyperskill.app.track_selection.list.view

import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.progress_screen.domain.model.averageRating
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature

internal class TrackSelectionListViewStateMapper(
    private val numbersFormatter: NumbersFormatter,
    private val dateFormatter: SharedDateFormatter
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
    ): TrackSelectionListFeature.ViewState {
        val sortedTracks = listOfNotNull(state.tracks.firstOrNull { it.track.id == state.selectedTrackId }) +
            state.tracks.filter { it.track.id != state.selectedTrackId }.sortedBy { it.trackProgress.rank }

        return TrackSelectionListFeature.ViewState.Content(
            tracks = sortedTracks.map {
                mapTrackToViewStateTrack(
                    trackWithProgress = it,
                    isSelected = it.track.id == state.selectedTrackId
                )
            }
        )
    }

    private fun mapTrackToViewStateTrack(
        trackWithProgress: TrackWithProgress,
        isSelected: Boolean
    ): TrackSelectionListFeature.ViewState.Track =
        TrackSelectionListFeature.ViewState.Track(
            id = trackWithProgress.track.id,
            imageSource = trackWithProgress.track.cover?.takeIf { it.isNotBlank() },
            title = trackWithProgress.track.title,
            timeToComplete = dateFormatter.formatHoursCount(trackWithProgress.track.secondsToComplete),
            rating = numbersFormatter.formatProgressAverageRating(trackWithProgress.trackProgress.averageRating()),
            isBeta = trackWithProgress.track.isBeta,
            isCompleted = trackWithProgress.track.isCompleted,
            isSelected = isSelected,
            progress = trackWithProgress.averageProgress
        )
}