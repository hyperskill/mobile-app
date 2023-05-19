package org.hyperskill.app.placeholder_new_user.view.mapper

import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.view.model.PlaceholderNewUserViewData
import org.hyperskill.app.track.domain.model.Track

class PlaceholderNewUserViewDataMapper(
    private val dateFormatter: SharedDateFormatter
) {
    fun mapStateToViewData(state: PlaceholderNewUserFeature.State.Content): PlaceholderNewUserViewData =
        PlaceholderNewUserViewData(
            tracks = state.tracks.sortedBy { it.progress?.rank }.map(::mapTrackToViewDataTrack)
        )

    fun mapTrackToViewDataTrack(track: Track): PlaceholderNewUserViewData.Track =
        PlaceholderNewUserViewData.Track(
            id = track.id,
            imageSource = track.cover ?: "",
            title = track.title,
            description = track.description,
            timeToComplete = dateFormatter.hoursCount(track.secondsToComplete) ?: "",
            rating = track.progress?.averageRating?.toString() ?: ""
        )
}