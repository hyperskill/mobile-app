package org.hyperskill.app.placeholder_new_user.view.model

class PlaceholderNewUserViewData(
    val tracks: List<Track>
) {
    @kotlinx.serialization.Serializable
    data class Track(
        val id: Long,
        val imageSource: String,
        val title: String,
        val description: String,
        val timeToComplete: String,
        val rating: String
    )
}
