package org.hyperskill.app.badges.data.source

import org.hyperskill.app.badges.domain.model.Badge

interface BadgesRemoteDataSource {
    suspend fun getReceivedBadges(): Result<List<Badge>>
}