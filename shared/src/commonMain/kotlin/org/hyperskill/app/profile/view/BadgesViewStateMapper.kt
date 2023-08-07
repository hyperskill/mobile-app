package org.hyperskill.app.profile.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.badges.domain.model.BadgeRank
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.profile.presentation.ProfileFeature

class BadgesViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    companion object {
        const val HIDDEN_STATE_BADGES_COUNT = 4
    }

    fun map(state: ProfileFeature.BadgesState): BadgesViewState {
        val unlockedBadges = state.badges.filter { it.level > 0 }.sortedBy { it.level }.map(::mapUnlockedBadge)
        val lockedBadges = getLockedBadgeKinds(unlockedBadges.map { it.kind }).map(::mapLockedBadge)
        val allBadges = unlockedBadges + lockedBadges

        return BadgesViewState(
            badges = allBadges.take(
                if (state.isExpanded) {
                    allBadges.size
                } else {
                    HIDDEN_STATE_BADGES_COUNT
                }
            ),
            isExpanded = state.isExpanded
        )
    }

    private fun getLockedBadgeKinds(unlockedBadgeKinds: List<BadgeKind>): Set<BadgeKind> =
        BadgeKind.values().filter { it != BadgeKind.UNKNOWN }.subtract(unlockedBadgeKinds.toSet())

    private fun mapUnlockedBadge(badge: Badge): BadgesViewState.BadgeViewState =
        BadgesViewState.BadgeViewState(
            kind = badge.kind,
            title = badge.title,
            image = BadgesViewState.BadgeImage.Remote(
                fullSource = badge.imageFull,
                previewSource = badge.imagePreview
            ),
            formattedCurrentLevel = resourceProvider.getString(
                if (badge.isMaxLevel) {
                    SharedResources.strings.badge_max_level
                } else {
                    SharedResources.strings.badge_level
                },
                badge.level
            ),
            nextLevel = getNextLevel(badge),
            progress = getProgress(badge)
        )

    private fun mapLockedBadge(badgeKind: BadgeKind): BadgesViewState.BadgeViewState =
        BadgesViewState.BadgeViewState(
            kind = badgeKind,
            title = getBadgeTitle(badgeKind) ?: "",
            image = BadgesViewState.BadgeImage.Locked,
            formattedCurrentLevel = resourceProvider.getString(SharedResources.strings.badge_level, 0),
            nextLevel = 1,
            progress = 0f
        )

    fun map(badgeDetails: ProfileFeature.Action.ViewAction.BadgeDetails): BadgeDetailsViewState =
        when (badgeDetails) {
            is ProfileFeature.Action.ViewAction.BadgeDetails.Badge -> mapToDetails(badgeDetails.badge)
            is ProfileFeature.Action.ViewAction.BadgeDetails.Kind -> mapToDetails(badgeDetails.badgeKind)
        }

    private fun mapToDetails(badge: Badge): BadgeDetailsViewState =
        BadgeDetailsViewState(
            kind = badge.kind,
            rank = badge.rank,
            title = badge.title,
            formattedRank = getBadgeRankName(badge.rank) ?: "",
            badgeDescription = if (!badge.isMaxLevel) {
                getBadgeDescription(badge.kind)
            } else {
                null
            },
            levelDescription = when {
                badge.level == 0 ->
                    badge.nextLevelValue?.let { nextLevelValue ->
                        getBadgeUnlockDescription(badge.kind, nextLevelValue)
                    }
                !badge.isMaxLevel ->
                    badge.nextLevelValue?.let { nextLevelValue ->
                        getBadgeUpgradeDescription(badge.kind, nextLevelValue)
                    }
                badge.isMaxLevel ->
                    resourceProvider.getString(SharedResources.strings.badge_max_level_description)
                else -> null
            },
            formattedCurrentLevel = if (badge.level == 0) {
                resourceProvider.getString(SharedResources.strings.badge_locked)
            } else {
                resourceProvider.getString(SharedResources.strings.badge_level, badge.level)
            },
            formattedNextLevel = getNextLevel(badge)?.let { nextLevel ->
                resourceProvider.getString(SharedResources.strings.badge_level, nextLevel)
            },
            progress = getProgress(badge),
            imageSource = badge.imageFull,
            isLocked = false // TODO: replace with `badge.rank == BadgeRank.LOCKED`
        )

    private fun mapToDetails(badgeKind: BadgeKind): BadgeDetailsViewState =
        BadgeDetailsViewState(
            kind = badgeKind,
            rank = BadgeRank.UNKNOWN, // TODO: replace with BadgeRank.LOCKED
            title = getBadgeTitle(badgeKind) ?: "",
            formattedRank = "", // TODO: replace with `getBadgeRankName(BadgeRank.LOCKED)`
            badgeDescription = getBadgeDescription(badgeKind),
            levelDescription = getBadgeUnlockDescription(badgeKind, countToUnlock = 1),
            formattedNextLevel = resourceProvider.getString(SharedResources.strings.badge_locked),
            formattedCurrentLevel = resourceProvider.getString(SharedResources.strings.badge_level, 1),
            progress = 0f,
            imageSource = null,
            isLocked = true
        )

    private fun getBadgeTitle(badgeKind: BadgeKind): String? =
        when (badgeKind) {
            BadgeKind.ProjectMaster ->
                SharedResources.strings.badge_project_mastery_title
            BadgeKind.TopicMaster ->
                SharedResources.strings.badge_topic_mastery
            BadgeKind.CommittedLearner ->
                SharedResources.strings.badge_committed_learning
            BadgeKind.BrilliantMind ->
                SharedResources.strings.badge_brilliant_mind
            BadgeKind.HelpingHand ->
                SharedResources.strings.badge_helping_hand
            BadgeKind.Sweetheart ->
                SharedResources.strings.badge_sweetheart
            BadgeKind.Benefactor ->
                SharedResources.strings.badge_benefactor
            BadgeKind.BountyHunter ->
                SharedResources.strings.badge_bounty_hunter
            BadgeKind.UNKNOWN -> null
        }?.let(resourceProvider::getString)

    private fun getBadgeRankName(badgeRank: BadgeRank): String? =
        when (badgeRank) {
            BadgeRank.APPRENTICE ->
                SharedResources.strings.badge_apprentice_level
            BadgeRank.EXPERT ->
                SharedResources.strings.badge_expert_level
            BadgeRank.MASTER -> SharedResources.strings.badge_master_level
            BadgeRank.LEGENDARY ->
                SharedResources.strings.badge_legendary_level
            BadgeRank.UNKNOWN -> null
        }?.let { res -> resourceProvider.getString(res) }

    private fun getBadgeDescription(badgeKind: BadgeKind): String? =
        when (badgeKind) {
            BadgeKind.ProjectMaster -> SharedResources.strings.badge_project_master_description
            BadgeKind.TopicMaster -> SharedResources.strings.badge_topic_master_description
            BadgeKind.CommittedLearner -> SharedResources.strings.badge_committed_learner_description
            BadgeKind.BrilliantMind -> SharedResources.strings.badge_brilliant_mind_description
            BadgeKind.HelpingHand -> SharedResources.strings.badge_helping_hand_description
            BadgeKind.Sweetheart -> SharedResources.strings.badge_sweetheart_description
            BadgeKind.Benefactor -> SharedResources.strings.badge_benefactor_description
            BadgeKind.BountyHunter -> SharedResources.strings.badge_bounty_hunter_description
            BadgeKind.UNKNOWN -> null
        }?.let(resourceProvider::getString)

    private fun getBadgeUnlockDescription(badgeKind: BadgeKind, countToUnlock: Int): String? =
        when (badgeKind) {
            BadgeKind.ProjectMaster -> SharedResources.plurals.badge_project_master_unlock_description
            BadgeKind.TopicMaster -> SharedResources.plurals.badge_topic_master_unlock_description
            BadgeKind.CommittedLearner -> SharedResources.plurals.badge_committed_learner_unlock_description
            BadgeKind.BrilliantMind -> SharedResources.plurals.badge_brilliant_mind_unlock_description
            BadgeKind.HelpingHand -> SharedResources.plurals.badge_helping_hand_unlock_description
            BadgeKind.Sweetheart -> SharedResources.plurals.badge_sweetheart_unlock_description
            BadgeKind.Benefactor -> SharedResources.plurals.badge_benefactor_unlock_description
            BadgeKind.BountyHunter -> SharedResources.plurals.badge_bounty_hunter_unlock_description
            BadgeKind.UNKNOWN -> null
        }?.let { res -> resourceProvider.getQuantityString(res, countToUnlock, countToUnlock) }

    private fun getBadgeUpgradeDescription(badgeKind: BadgeKind, countToUpgrade: Int): String? =
        when (badgeKind) {
            BadgeKind.ProjectMaster -> SharedResources.plurals.badge_project_master_upgrade_description
            BadgeKind.TopicMaster -> SharedResources.plurals.badge_topic_master_upgrade_description
            BadgeKind.CommittedLearner -> SharedResources.plurals.badge_committed_learner_upgrade_description
            BadgeKind.BrilliantMind -> SharedResources.plurals.badge_brilliant_mind_upgrade_description
            BadgeKind.HelpingHand -> SharedResources.plurals.badge_helping_hand_upgrade_description
            BadgeKind.Sweetheart -> SharedResources.plurals.badge_sweetheart_upgrade_description
            BadgeKind.Benefactor -> SharedResources.plurals.badge_benefactor_upgrade_description
            BadgeKind.BountyHunter -> SharedResources.plurals.badge_bounty_hunter_upgrade_description
            BadgeKind.UNKNOWN -> null
        }?.let { res -> resourceProvider.getQuantityString(res, countToUpgrade, countToUpgrade) }

    private fun getNextLevel(badge: Badge): Int? =
        if (badge.isMaxLevel) null else badge.level + 1

    private fun getProgress(badge: Badge): Float =
        if (badge.nextLevelValue != null && !badge.isMaxLevel) {
            val totalCount = badge.nextLevelValue - badge.currentLevelValue
            val currentCount = badge.value - badge.currentLevelValue
            currentCount / totalCount.toFloat()
        } else {
            0f
        }
}