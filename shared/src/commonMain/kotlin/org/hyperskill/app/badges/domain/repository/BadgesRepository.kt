package org.hyperskill.app.badges.domain.repository

import org.hyperskill.app.badges.domain.model.Badge

interface BadgesRepository {
    suspend fun getReceivedBadges(): Result<List<Badge>>
    suspend fun getBadge(badgeId: Long): Result<Badge>
}