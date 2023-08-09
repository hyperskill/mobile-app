package org.hyperskill.app.profile.view

sealed interface BadgeImage {
    object Locked : BadgeImage

    data class Remote(
        val fullSource: String,
        val previewSource: String
    ) : BadgeImage
}