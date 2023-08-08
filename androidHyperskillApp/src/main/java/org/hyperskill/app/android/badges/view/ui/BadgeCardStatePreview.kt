package org.hyperskill.app.android.badges.view.ui

import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.profile.view.BadgesViewState

object BadgeCardStatePreview {
    fun getShortTitlePreview(): BadgesViewState.Badge =
        getPreview(title = "Project Mastery", isMaxLevel = false)

    fun getLongTitlePreview(): BadgesViewState.Badge =
        getPreview(title = "Project Mastery Project Mastery", isMaxLevel = true)

    fun getAllBadgesPreview(): List<BadgesViewState.Badge> =
        BadgeKind.values()
            .toList()
            .minus(BadgeKind.UNKNOWN)
            .mapIndexed { index, badgeKind ->
                getPreview(
                    title = if (index % 2 == 0) {
                        "Project Mastery"
                    } else {
                        "Project Mastery Project Mastery Project"
                    },
                    kind = badgeKind,
                    isMaxLevel = index % 2 == 0
                )
            }

    private fun getPreview(
        title: String,
        kind: BadgeKind = BadgeKind.TopicMaster,
        isMaxLevel: Boolean
    ): BadgesViewState.Badge =
        BadgesViewState.Badge(
            kind = kind,
            title = title,
            image = BadgesViewState.BadgeImage.Locked,
            formattedCurrentLevel = if (isMaxLevel) "Level 15 (max)" else "Level 1",
            nextLevel = if (isMaxLevel) null else 2,
            progress = .35f
        )
}