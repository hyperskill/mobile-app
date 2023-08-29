package org.hyperskill.app.badges.data.repository

import org.hyperskill.app.badges.data.source.BadgesRemoteDataSource
import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.badges.domain.repository.BadgesRepository

internal class BadgesRepositoryImpl(
    private val remoteDataSource: BadgesRemoteDataSource
) : BadgesRepository {
    override suspend fun getReceivedBadges(): Result<List<Badge>> =
        remoteDataSource.getReceivedBadges()

    override suspend fun getBadge(badgeId: Long): Result<Badge> =
        remoteDataSource.getBadge(badgeId)
}