package org.hyperskill.app.badges.domain.data.source

import org.hyperskill.app.badges.domain.model.Badge

interface BadgesDataSource {
    suspend fun getReceivedBadges(): Result<List<Badge>>
}