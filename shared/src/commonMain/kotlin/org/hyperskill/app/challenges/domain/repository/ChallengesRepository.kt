package org.hyperskill.app.challenges.domain.repository

import org.hyperskill.app.challenges.domain.model.Challenge

interface ChallengesRepository {
    suspend fun getChallenges(): Result<List<Challenge>>
}