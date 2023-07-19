package org.hyperskill.app.profile.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.profile.presentation.ProfileFeature

class BadgesViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: ProfileFeature.BadgesState): BadgesViewState {
        val unlockedBadges = state.badges.sortedBy { it.level }.map(::mapUnlockedBadge)
        return if (state.isExpanded) {
            val lockedBadgeKinds = getLockedBadgeKinds(state.badges.map { it.kind })
            val lockedBadges = lockedBadgeKinds.map(::mapLockedBadge)
            BadgesViewState(
                badges = lockedBadges,
                isExpanded = state.isExpanded
            )
        } else {
            BadgesViewState(
                badges = unlockedBadges,
                isExpanded = state.isExpanded
            )
        }
    }

    private fun getLockedBadgeKinds(unlockedBadgeKinds: List<BadgeKind>): Set<BadgeKind> =
        BadgeKind.values().subtract(unlockedBadgeKinds.toSet())

    private fun mapUnlockedBadge(badge: Badge): BadgesViewState.BadgeViewState =
        BadgesViewState.BadgeViewState(
            kind = badge.kind,
            title = badge.title,
            image = BadgesViewState.BadgeImage.Remote(""), // TODO: add real data
            formattedCurrentLevel = resourceProvider.getString(
                if (badge.isMaxLevel) {
                    SharedResources.strings.badge_max_level
                } else {
                    SharedResources.strings.badge_level
                },
                badge.level
            ),
            nextLevel = if (badge.isMaxLevel) null else badge.level + 1,
            progress = if (badge.nextLevelValue != null && badge.isMaxLevel) {
                val totalCount = badge.nextLevelValue - badge.currentLevelValue
                val currentCount = badge.value - badge.currentLevelValue
                currentCount / totalCount.toFloat()
            } else {
                1f
            }
        )

    private fun mapLockedBadge(badgeKind: BadgeKind): BadgesViewState.BadgeViewState =
        BadgesViewState.BadgeViewState(
            kind = badgeKind,
            title = getBadgeTitle(badgeKind),
            image = BadgesViewState.BadgeImage.Locked,
            formattedCurrentLevel = resourceProvider.getString(SharedResources.strings.badge_level, 0),
            nextLevel = 1,
            progress = 0f
        )

    private fun getBadgeTitle(badgeKind: BadgeKind): String {
        val stringResource = when (badgeKind) {
            BadgeKind.ProjectMaster -> SharedResources.strings.badge_project_mastery_title
            BadgeKind.TopicMaster -> SharedResources.strings.badge_project_topic_mastery
            BadgeKind.CommittedLearner -> SharedResources.strings.badge_project_committed_learning
            BadgeKind.BrilliantMind -> SharedResources.strings.badge_project_brilliant_mind
            BadgeKind.HelpingHand -> SharedResources.strings.badge_project_helping_hand
            BadgeKind.Sweetheart -> SharedResources.strings.badge_project_sweetheart
            BadgeKind.Benefactor -> SharedResources.strings.badge_project_benefactor
            BadgeKind.BountyHunter -> SharedResources.strings.badge_project_bounty_hunter
        }
        return resourceProvider.getString(stringResource)
    }
}