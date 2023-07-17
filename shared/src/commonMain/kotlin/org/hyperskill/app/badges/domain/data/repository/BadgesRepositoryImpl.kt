package org.hyperskill.app.badges.domain.data.repository

import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.badges.remote.BadgesRemoteDataSource

class BadgesRepositoryImpl(
    private val remoteDataSource: BadgesRemoteDataSource
) : BadgesRepository {
    override suspend fun getReceivedBadges(): Result<List<Badge>> =
        remoteDataSource.getReceivedBadges()
}