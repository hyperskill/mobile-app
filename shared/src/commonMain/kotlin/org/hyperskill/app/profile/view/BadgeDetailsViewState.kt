package org.hyperskill.app.profile.view

import org.hyperskill.app.badges.domain.model.BadgeKind

data class BadgeDetailsViewState(
    val kind: BadgeKind,
    val title: String,
    val rank: String,
    val imageSource: String?,
    val badgeDescription: String?,
    val levelDescription: String?,
    val formattedCurrentLevel: String,
    val formattedNextLevel: String?,
    val progress: Float?,
    val isLocked: Boolean
)