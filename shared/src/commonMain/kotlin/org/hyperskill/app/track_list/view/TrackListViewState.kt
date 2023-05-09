package org.hyperskill.app.track_list.view

sealed interface TrackListViewState {
    object Idle : TrackListViewState

    object Loading : TrackListViewState

    object Error : TrackListViewState

    data class Content(
        val tracks: List<Track>
    ) : TrackListViewState

    @kotlinx.serialization.Serializable
    data class Track(
        val id: Long,
        val imageSource: String,
        val title: String,
        val description: String,
        val timeToComplete: String,
        val rating: String,
        val isSelected: Boolean
    )
}