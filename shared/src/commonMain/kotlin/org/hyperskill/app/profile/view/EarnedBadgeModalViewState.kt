package org.hyperskill.app.profile.view

import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.badges.domain.model.BadgeRank

data class EarnedBadgeModalViewState(
    val kind: BadgeKind,
    val rank: BadgeRank,
    val formattedRank: String,
    val image: BadgeImage,
    val title: String,
    val description: String
)