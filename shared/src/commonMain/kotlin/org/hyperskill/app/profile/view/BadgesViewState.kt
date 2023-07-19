package org.hyperskill.app.profile.view

import org.hyperskill.app.badges.domain.model.BadgeKind

data class BadgesViewState(
    val badges: List<BadgeViewState>,
    val isExpanded: Boolean
) {
    /**
     * Represent a badge in profile.
     *
     * [progress] is progress on the way to the next level. It is a float value from 0f to 1f.
     * [nextLevel] represents a next level number. If null, then this level is the max one.
     */
    data class BadgeViewState(
        val kind: BadgeKind,
        val title: String,
        val image: BadgeImage,
        val formattedCurrentLevel: String,
        val nextLevel: Int?,
        val progress: Float
    )

    sealed interface BadgeImage {
        object Locked : BadgeImage

        data class Remote(
            val fullSource: String,
            val previewSource: String
        ) : BadgeImage
    }
}