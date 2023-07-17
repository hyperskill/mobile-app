package org.hyperskill.app.badges.domain.data.repository

import org.hyperskill.app.SharedResources
import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.badges.domain.model.BadgeType
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.badges.remote.BadgesRemoteDataSource
import org.hyperskill.app.core.view.mapper.ResourceProvider

class BadgesRepositoryImpl(
    private val remoteDataSource: BadgesRemoteDataSource,
    private val resourceProvider: ResourceProvider
) : BadgesRepository {
    override suspend fun getReceivedBadges(): Result<List<Badge>> =
        remoteDataSource.getReceivedBadges().map { unlockedBadges ->
            unlockedBadges.sortedBy { it.level } +
                getLockedBadges(unlockedBadges.map { it.kind })
        }

    private fun getLockedBadges(unlockedBadgeTypes: List<BadgeType>): List<Badge> {
        fun getBadgeTitle(type: BadgeType): String {
            val stringResource = when (type) {
                BadgeType.ProjectMaster -> SharedResources.strings.badge_project_mastery_title
                BadgeType.TopicMaster -> SharedResources.strings.badge_project_topic_mastery
                BadgeType.CommittedLearner -> SharedResources.strings.badge_project_committed_learning
                BadgeType.BrilliantMind -> SharedResources.strings.badge_project_brilliant_mind
                BadgeType.HelpingHand -> SharedResources.strings.badge_project_helping_hand
                BadgeType.Sweetheart -> SharedResources.strings.badge_project_sweetheart
                BadgeType.Benefactor -> SharedResources.strings.badge_project_benefactor
                BadgeType.BountyHunter -> SharedResources.strings.badge_project_bounty_hunter
            }
            return resourceProvider.getString(stringResource)
        }

        return BadgeType.values()
            .filter { it in unlockedBadgeTypes }
            .map { lockedBadgeType ->
                Badge(
                    id = 0,
                    kind = lockedBadgeType,
                    title = getBadgeTitle(lockedBadgeType),
                    level = 0,
                    value = 0,
                    currentLevelValue = 0,
                    nextLevelValue = 1,
                    isMaxLevel = false
                )
            }
    }
}