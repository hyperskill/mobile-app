package org.hyperskill.app.profile.view

import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.badges.domain.model.BadgeRank

data class BadgeDetailsViewState(
    val kind: BadgeKind,
    val rank: BadgeRank,
    val title: String,
    val formattedRank: String,
    val image: BadgeImage,
    val badgeDescription: String?,
    val levelDescription: String?,
    val formattedCurrentLevel: String,
    val formattedNextLevel: String?,
    val progress: Float,
    val isLocked: Boolean
)