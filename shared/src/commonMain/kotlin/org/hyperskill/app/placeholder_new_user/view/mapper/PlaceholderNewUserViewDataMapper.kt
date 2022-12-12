package org.hyperskill.app.placeholder_new_user.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.view.model.PlaceholderNewUserViewData
import org.hyperskill.app.track.domain.model.Track
import kotlin.math.floor

class PlaceholderNewUserViewDataMapper(
    private val resourceProvider: ResourceProvider
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
            timeToComplete = getTimeToComplete(track.secondsToComplete),
            rating = track.progress?.averageRating?.toString() ?: ""
        )

    private fun getTimeToComplete(secondsToComplete: Double): String {
        val hours = floor(secondsToComplete / 3600).toInt()

        return resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
    }
}