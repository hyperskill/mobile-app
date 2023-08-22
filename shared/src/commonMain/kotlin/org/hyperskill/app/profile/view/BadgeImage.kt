package org.hyperskill.app.profile.view

sealed interface BadgeImage {
    object Locked : BadgeImage

    data class Remote(val source: String) : BadgeImage
}