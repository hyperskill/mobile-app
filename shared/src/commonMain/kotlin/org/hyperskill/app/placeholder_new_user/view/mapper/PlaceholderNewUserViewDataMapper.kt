package org.hyperskill.app.placeholder_new_user.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.view.model.PlaceholderNewUserTrack
import kotlin.math.floor

class PlaceholderNewUserViewDataMapper(
    private val resourceProvider: ResourceProvider
) {
    fun mapStateToTracks(state: PlaceholderNewUserFeature.State.Content): List<PlaceholderNewUserTrack> =
        state.tracks.map { track ->
            PlaceholderNewUserTrack(
                id = track.id,
                imageSource = track.cover ?: "",
                title = track.title,
                description = track.description,
                timeToComplete = getTimeToComplete(track.secondsToComplete),
                rating = state.tracksProgresses[track.id]?.averageRating?.toString() ?: ""
            )
        }

    private fun getTimeToComplete(secondsToComplete: Double): String {
        val hours = floor(secondsToComplete / 3600).toInt()

        return resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
    }
}