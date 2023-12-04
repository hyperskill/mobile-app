package org.hyperskill.app.leaderboard.injection

import org.hyperskill.app.leaderboard.domain.repository.LeaderboardRepository

interface LeaderboardDataComponent {
    val leaderboardRepository: LeaderboardRepository
}