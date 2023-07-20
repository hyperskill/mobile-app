package org.hyperskill.app.badges.data.repository

import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.badges.remote.BadgesRemoteDataSourceImpl

internal class BadgesRepositoryImpl(
    private val remoteDataSource: BadgesRemoteDataSourceImpl
) : BadgesRepository {
    override suspend fun getReceivedBadges(): Result<List<Badge>> =
        remoteDataSource.getReceivedBadges()
}